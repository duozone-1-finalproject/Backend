package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.FastApiRequestDto;
import com.example.finalproject.ai_backend.dto.FastApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class FastApiCommunicationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${fastapi.kafka.timeout.seconds}")
    private int timeoutSeconds;

    // 요청-응답 매핑
    private final Map<String, CompletableFuture<FastApiResponseDto>> pendingRequests = new ConcurrentHashMap<>();

    // FastAPI 전용 Kafka 토픽
    private static final String FASTAPI_REQUEST_TOPIC = "fastapi-equity-request";
    private static final String FASTAPI_RESPONSE_TOPIC = "fastapi-equity-response";

    public FastApiCommunicationService(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * FastAPI에게 주식 공모 주석 생성 요청 전송
     */
    public CompletableFuture<FastApiResponseDto> requestEquityAnnotation(FastApiRequestDto request) {
        try {
            String requestId = request.getRequestId();
            log.info("FastAPI에게 주식 공모 주석 생성 요청 전송: {}, company={}",
                    requestId, request.getCompanyName());

            CompletableFuture<FastApiResponseDto> future = new CompletableFuture<>();
            pendingRequests.put(requestId, future);

            // 타임아웃 설정 - 예외 처리 개선
            future.orTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .whenComplete((result, throwable) -> {
                        if (throwable instanceof TimeoutException) {
                            log.error("FastAPI 요청 타임아웃: {} ({}초 초과)", requestId, timeoutSeconds);
                            pendingRequests.remove(requestId);
                        } else if (throwable != null) {
                            log.error("FastAPI 요청 처리 중 오류: {}", requestId, throwable);
                            pendingRequests.remove(requestId);
                        }
                    });

            // Kafka로 요청 전송
            String jsonRequest = objectMapper.writeValueAsString(request);
            kafkaTemplate.send(FASTAPI_REQUEST_TOPIC, requestId, jsonRequest)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Kafka 메시지 전송 실패: {}", requestId, ex);
                            if (!future.isDone()) {
                                future.completeExceptionally(ex);
                            }
                            pendingRequests.remove(requestId);
                        } else {
                            log.info("Kafka 메시지 전송 성공: {}", requestId);
                        }
                    });

            return future;

        } catch (Exception e) {
            log.error("FastAPI 요청 전송 실패", e);
            CompletableFuture<FastApiResponseDto> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    /**
     * FastAPI 응답 수신 - 수정된 시그니처
     */
    @KafkaListener(topics = FASTAPI_RESPONSE_TOPIC, groupId = "${spring.kafka.consumer.fastapi-group-id}")
    public void handleFastApiResponse(ConsumerRecord<String, String> record) {
        try {
            String requestId = record.key();
            String message = record.value();

            log.info("FastAPI 서버 응답 수신: {}",
                    message.length() > 200 ? message.substring(0, 200) + "..." : message);

            handleFastApiResponseInternal(message);

        } catch (Exception e) {
            log.error("Kafka 메시지 처리 중 오류", e);
        }
    }

    /**
     * 응답 처리 내부 로직
     */
    private void handleFastApiResponseInternal(String message) {
        try {
            FastApiResponseDto response = objectMapper.readValue(message, FastApiResponseDto.class);
            String requestId = response.getRequestId();

            if (requestId == null || requestId.isEmpty()) {
                log.warn("응답에 requestId가 없습니다: {}", message);
                return;
            }

            CompletableFuture<FastApiResponseDto> pendingRequest = pendingRequests.remove(requestId);

            if (pendingRequest != null) {
                if (!pendingRequest.isDone()) {
                    pendingRequest.complete(response);
                    log.info("FastAPI 응답 처리 완료: {}, status={}", requestId, response.getStatus());
                } else {
                    log.warn("이미 완료된 요청: {}", requestId);
                }
            } else {
                log.warn("매칭되는 대기 중인 요청을 찾을 수 없음: {} (총 대기 중: {})",
                        requestId, pendingRequests.size());

                // 현재 대기 중인 요청 ID들을 로그로 출력 (디버깅용)
                if (log.isDebugEnabled()) {
                    log.debug("현재 대기 중인 요청들: {}", pendingRequests.keySet());
                }
            }

        } catch (Exception e) {
            log.error("FastAPI 응답 처리 실패: {}", e.getMessage(), e);
        }
    }

    /**
     * Kafka 연결 상태 확인
     */
    public CompletableFuture<Boolean> checkKafkaConnection() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String testMessage = "fastapi-health-check-" + System.currentTimeMillis();
                kafkaTemplate.send("fastapi-health-check", "test", testMessage).get(5, TimeUnit.SECONDS);
                log.info("FastAPI Kafka 연결 정상");
                return true;
            } catch (Exception e) {
                log.error("FastAPI Kafka 연결 실패: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 대기 중인 요청 수 조회
     */
    public int getPendingRequestCount() {
        return pendingRequests.size();
    }

    /**
     * 요청 취소
     */
    public boolean cancelRequest(String requestId) {
        CompletableFuture<FastApiResponseDto> pendingRequest = pendingRequests.remove(requestId);
        if (pendingRequest != null && !pendingRequest.isDone()) {
            pendingRequest.cancel(true);
            log.info("FastAPI 요청 취소됨: {}", requestId);
            return true;
        }
        return false;
    }

    /**
     * 모든 대기 중인 요청 취소
     */
    public void cancelAllRequests() {
        log.info("모든 대기 중인 FastAPI 요청 취소 시작: {}개", pendingRequests.size());

        pendingRequests.forEach((requestId, future) -> {
            try {
                if (!future.isDone()) {
                    future.cancel(true);
                    log.debug("FastAPI 요청 취소: {}", requestId);
                }
            } catch (Exception e) {
                log.warn("FastAPI 요청 취소 실패: {}", requestId, e);
            }
        });

        pendingRequests.clear();
        log.info("모든 FastAPI 요청 취소 완료");
    }

    /**
     * 서비스 상태 모니터링
     */
    public Map<String, Object> getServiceStatus() {
        return Map.of(
                "pendingRequestCount", pendingRequests.size(),
                "timeoutSeconds", timeoutSeconds,
                "pendingRequestIds", pendingRequests.keySet()
        );
    }
}