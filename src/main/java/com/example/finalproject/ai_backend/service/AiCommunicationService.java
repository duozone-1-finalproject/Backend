// src/main/java/com/example/finalproject/ai_backend/service/AiCommunicationService.java
package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.AiRequestDto;
import com.example.finalproject.ai_backend.dto.AiResponseDto;
import com.example.finalproject.apitest.dto.common.AllDartDataResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AiCommunicationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ai.timeout.seconds:60}")
    private int timeoutSeconds;

    // 요청-응답 매핑
    private final Map<String, CompletableFuture<AiResponseDto>> pendingRequests = new ConcurrentHashMap<>();
    private final Map<String, AllDartDataResponse> requestAllData = new ConcurrentHashMap<>();

    private static final String AI_REQUEST_TOPIC = "ai-report-request";
    private static final String AI_RESPONSE_TOPIC = "ai-report-response";

    public AiCommunicationService(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * AI에게 보고서 생성 요청 전송
     */
    public CompletableFuture<AiResponseDto> requestReportGeneration(AiRequestDto request) {
        try {
            String requestId = request.getRequestId();
            log.info("AI에게 보고서 생성 요청 전송: {}", requestId);

            // 회사 전체 데이터 저장
            requestAllData.put(requestId, request.getAllDartData());

            CompletableFuture<AiResponseDto> future = new CompletableFuture<>();
            pendingRequests.put(requestId, future);

            future.orTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .whenComplete((result, throwable) -> {
                        if (throwable != null) {
                            log.error("AI 요청 타임아웃: {}", requestId, throwable);
                            pendingRequests.remove(requestId);
                            requestAllData.remove(requestId);
                        }
                    });

            String jsonRequest = objectMapper.writeValueAsString(request);
            kafkaTemplate.send(AI_REQUEST_TOPIC, requestId, jsonRequest)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Kafka 메시지 전송 실패: {}", requestId, ex);
                            future.completeExceptionally(ex);
                            pendingRequests.remove(requestId);
                            requestAllData.remove(requestId);
                        } else {
                            log.info("Kafka 메시지 전송 성공: {}", requestId);
                        }
                    });

            return future;

        } catch (Exception e) {
            log.error("AI 요청 전송 실패", e);
            CompletableFuture<AiResponseDto> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    /**
     * AI 응답 수신
     */
    @KafkaListener(topics = AI_RESPONSE_TOPIC, groupId = "ai-backend-group")
    public void handleAiResponse(String message) {
        log.info("AI 서버 응답 수신: {}", message.substring(0, Math.min(message.length(), 100)) + "...");
        handleAiResponseInternal(message);
    }

    private void handleAiResponseInternal(String message) {
        try {
            AiResponseDto response = objectMapper.readValue(message, AiResponseDto.class);
            String requestId = response.getRequestId();

            CompletableFuture<AiResponseDto> pendingRequest = pendingRequests.remove(requestId);
            requestAllData.remove(requestId);

            if (pendingRequest != null && !pendingRequest.isDone()) {
                pendingRequest.complete(response);
                log.info("AI 응답 처리 완료: {}", requestId);
            } else {
                log.warn("매칭되는 요청을 찾을 수 없음: {}", requestId);
            }

        } catch (Exception e) {
            log.error("AI 응답 처리 실패: {}", e.getMessage(), e);
        }
    }

    public CompletableFuture<Boolean> checkKafkaConnection() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String testMessage = "health-check-" + System.currentTimeMillis();
                kafkaTemplate.send("health-check", "test", testMessage).get(5, TimeUnit.SECONDS);
                log.info("Kafka 연결 정상");
                return true;
            } catch (Exception e) {
                log.error("Kafka 연결 실패: {}", e.getMessage());
                return false;
            }
        });
    }

    public int getPendingRequestCount() {
        return pendingRequests.size();
    }

    public boolean cancelRequest(String requestId) {
        CompletableFuture<AiResponseDto> pendingRequest = pendingRequests.remove(requestId);
        requestAllData.remove(requestId);
        if (pendingRequest != null && !pendingRequest.isDone()) {
            pendingRequest.cancel(true);
            log.info("요청 취소됨: {}", requestId);
            return true;
        }
        return false;
    }
}
