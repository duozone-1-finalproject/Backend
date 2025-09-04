package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.AiRequestDto;
import com.example.finalproject.ai_backend.dto.AiResponseDto;
import com.example.finalproject.ai_backend.dto.CompanyDataDto2;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AiCommunicationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final OpenSearchService openSearchService;

    @Value("${ai.mock.enabled:false}")
    private boolean mockEnabled;

    @Value("${ai.timeout.seconds:60}")
    private int timeoutSeconds;

    // 요청-응답 매핑을 위한 임시 저장소
    private final Map<String, CompletableFuture<AiResponseDto>> pendingRequests = new ConcurrentHashMap<>();
    private final Map<String, CompanyDataDto2> requestCompanyData = new ConcurrentHashMap<>();

    private static final String AI_REQUEST_TOPIC = "ai-report-request";
    private static final String AI_RESPONSE_TOPIC = "ai-report-response";

    public AiCommunicationService(KafkaTemplate<String, String> kafkaTemplate,
                                  ObjectMapper objectMapper,
                                  OpenSearchService openSearchService) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.openSearchService = openSearchService;
    }

    /**
     * AI에게 보고서 생성 요청 전송
     */
    public CompletableFuture<AiResponseDto> requestReportGeneration(AiRequestDto request) {
        try {
            String requestId = request.getRequestId();
            log.info("AI에게 보고서 생성 요청 전송: {}", requestId);

            // 회사 데이터 저장 (나중에 OpenSearch 저장용)
            requestCompanyData.put(requestId, request.getCompanyData());

            CompletableFuture<AiResponseDto> future = new CompletableFuture<>();
            pendingRequests.put(requestId, future);

            future.orTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .whenComplete((result, throwable) -> {
                        if (throwable != null) {
                            log.error("AI 요청 타임아웃: {}", requestId, throwable);
                            pendingRequests.remove(requestId);
                            requestCompanyData.remove(requestId);
                        }
                    });

            String jsonRequest = objectMapper.writeValueAsString(request);
            kafkaTemplate.send(AI_REQUEST_TOPIC, requestId, jsonRequest)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Kafka 메시지 전송 실패: {}", requestId, ex);
                            future.completeExceptionally(ex);
                            pendingRequests.remove(requestId);
                            requestCompanyData.remove(requestId);
                        } else {
                            log.info("Kafka 메시지 전송 성공: {}", requestId);
                            if (mockEnabled) {
                                generateAndSendMockResponse(request);
                            }
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
     * AI 응답 수신 및 OpenSearch 저장
     */
    @KafkaListener(topics = AI_RESPONSE_TOPIC, groupId = "ai-backend-group")
    public void handleAiResponse(String message) {
        log.info("AI 서버 응답 수신: {}", message.substring(0, Math.min(message.length(), 100)) + "...");
        handleAiResponseInternal(message);
    }

    /**
     * AI 응답 처리 및 OpenSearch 저장
     */
    private void handleAiResponseInternal(String message) {
        try {
            AiResponseDto response = objectMapper.readValue(message, AiResponseDto.class);
            String requestId = response.getRequestId();

            CompletableFuture<AiResponseDto> pendingRequest = pendingRequests.remove(requestId);
            CompanyDataDto2 companyData = requestCompanyData.remove(requestId);

            if (pendingRequest != null && !pendingRequest.isDone()) {
                // ★★★ OpenSearch에 저장 ★★★
                if (companyData != null) {
                    log.info("OpenSearch 저장 시작: requestId={}", requestId);

                    // 비동기로 OpenSearch에 저장
                    CompletableFuture.runAsync(() -> {
                        try {
                            openSearchService.saveGeneratedReport(response, companyData).get();
                            log.info("OpenSearch 저장 완료: {}", requestId);
                        } catch (Exception e) {
                            log.error("OpenSearch 저장 실패: {}", requestId, e);
                        }
                    });
                } else {
                    log.warn("회사 데이터가 없어 OpenSearch 저장 불가: {}", requestId);
                }

                pendingRequest.complete(response);
                log.info("AI 응답 처리 완료: {}", requestId);
            } else {
                log.warn("매칭되는 요청을 찾을 수 없음: {}", requestId);
            }

        } catch (Exception e) {
            log.error("AI 응답 처리 실패: {}", e.getMessage(), e);
        }
    }

    /**
     * Mock 응답 생성
     */
    @Profile({"dev", "test"})
    private void generateAndSendMockResponse(AiRequestDto request) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000); // 3초 대기
                AiResponseDto mockResponse = createMockResponse(request);
                String mockResponseJson = objectMapper.writeValueAsString(mockResponse);
                log.info("Mock AI 응답 생성: {}", request.getRequestId());
                handleAiResponseInternal(mockResponseJson);
            } catch (Exception e) {
                log.error("Mock 응답 생성 실패: {}", request.getRequestId(), e);
                CompletableFuture<AiResponseDto> pendingRequest = pendingRequests.remove(request.getRequestId());
                if (pendingRequest != null && !pendingRequest.isDone()) {
                    pendingRequest.completeExceptionally(e);
                }
            }
        });
    }

    /**
     * Mock 응답 데이터 생성
     */
    private AiResponseDto createMockResponse(AiRequestDto request) {
        String mockSummary = String.format(
                "%s는 테스트 회사입니다. Mock 데이터로 생성된 보고서입니다.",
                request.getCompanyData().getCorpName()
        );

        String mockHtml = String.format("""
                <div class="report">
                    <h1>%s 기업분석보고서</h1>
                    <h2>회사 개요</h2>
                    <p>회사명: %s</p>
                    <p>대표이사: %s</p>
                    <p>주소: %s</p>
                    <h2>분석 요약</h2>
                    <p>%s</p>
                    <p>생성시간: %s</p>
                </div>
                """,
                request.getCompanyData().getCorpName(),
                request.getCompanyData().getCorpName(),
                request.getCompanyData().getCeoName(),
                request.getCompanyData().getAddress(),
                mockSummary,
                java.time.LocalDateTime.now()
        );

        return AiResponseDto.builder()
                .requestId(request.getRequestId())
                .generatedHtml(mockHtml)
                .summary(mockSummary)
                .processingTime(3000L)
                .status("SUCCESS")
                .build();
    }

    /**
     * Kafka 연결 상태 확인
     */
    public CompletableFuture<Boolean> checkKafkaConnection() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String testMessage = "health-check-" + System.currentTimeMillis();
                kafkaTemplate.send("health-check", "test", testMessage).get(10, TimeUnit.SECONDS);
                return true;
            } catch (Exception e) {
                log.error("Kafka 연결 실패: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 동기 Kafka 연결 테스트
     */
    public boolean testKafkaConnectionSync() {
        try {
            String testMessage = "sync-health-check-" + System.currentTimeMillis();
            kafkaTemplate.send("health-check", "sync-test", testMessage);
            kafkaTemplate.flush();
            return true;
        } catch (Exception e) {
            log.error("동기 Kafka 연결 테스트 실패: {}", e.getMessage(), e);
            return false;
        }
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
        CompletableFuture<AiResponseDto> pendingRequest = pendingRequests.remove(requestId);
        requestCompanyData.remove(requestId);
        if (pendingRequest != null && !pendingRequest.isDone()) {
            pendingRequest.cancel(true);
            log.info("요청 취소됨: {}", requestId);
            return true;
        }
        return false;
    }
}