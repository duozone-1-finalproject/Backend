package com.example.finalproject.ai_backend.controller;

import com.example.finalproject.ai_backend.dto.ApiResponseDto2;
import com.example.finalproject.ai_backend.dto.AiRequestDto;
import com.example.finalproject.ai_backend.dto.CompanyDataDto2;
import com.example.finalproject.ai_backend.service.AiCommunicationService;
import com.example.finalproject.ai_backend.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/kafka-test")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class KafkaTestController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AiCommunicationService aiCommunicationService;
    private final TemplateService templateService;

    @Value("${ai.mock.enabled:false}")
    private boolean mockEnabled;

    @Value("${ai.timeout.seconds:60}")
    private int timeoutSeconds;

    // ========== 실제 AI 서버(8081) 연동 테스트 ==========

    /**
     * 실제 회사 데이터로 AI 연동 테스트 (8080 → 8081)
     * POST /api/v1/kafka-test/test-with-company-data
     */
    @PostMapping("/test-with-company-data")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<Object>>> testWithCompanyData(
            @RequestBody CompanyDataDto2 companyData) {

        log.info("실제 회사 데이터 AI 연동 테스트 시작: {} (Mock모드: {})",
                companyData.getCorpName(), mockEnabled);

        try {
            String requestId = "REAL_" + UUID.randomUUID().toString().substring(0, 8);

            // 템플릿 처리
            String template = templateService.getSecuritiesRegistrationTemplate();
            Map<String, String> variables = templateService.createTemplateVariables(companyData);
            String processedTemplate = templateService.applyVariablesToTemplate(template, variables);

            // AI 요청 DTO 생성
            AiRequestDto aiRequest = AiRequestDto.builder()
                    .requestId(requestId)
                    .template(processedTemplate)
                    .companyData(companyData)
                    .build();

            log.info("AI 서버(8081) 연동 요청 전송: requestId={}, company={}, mockMode={}",
                    requestId, companyData.getCorpName(), mockEnabled);

            // 실제 Kafka를 통한 AI 요청 (8080 → Kafka → 8081)
            return aiCommunicationService.requestReportGeneration(aiRequest)
                    .thenApply(aiResponse -> {
                        log.info("AI 서버 연동 성공: requestId={}, status={}, mockUsed={}",
                                aiResponse.getRequestId(), aiResponse.getStatus(), mockEnabled);

                        // Null-safe 처리
                        String generatedHtml = aiResponse.getGeneratedHtml();
                        String summary = aiResponse.getSummary();

                        Map<String, Object> responseData = new HashMap<>();
                        responseData.put("request_id", aiResponse.getRequestId() != null ? aiResponse.getRequestId() : requestId);
                        responseData.put("status", aiResponse.getStatus() != null ? aiResponse.getStatus() : "UNKNOWN");
                        responseData.put("processing_time", aiResponse.getProcessingTime() != null ? aiResponse.getProcessingTime() : "N/A");
                        responseData.put("summary", summary != null ? summary : "요약 정보 없음");
                        responseData.put("html_preview", truncateString(generatedHtml, 500));
                        responseData.put("full_html_length", generatedHtml != null ? generatedHtml.length() : 0);
                        responseData.put("company_name", companyData.getCorpName());
                        responseData.put("test_type", "REAL_AI_INTEGRATION");
                        responseData.put("mock_mode", mockEnabled);
                        responseData.put("ai_server_port", 8081);
                        responseData.put("html_status", generatedHtml != null ? "AVAILABLE" : "NULL_RESPONSE");

                        String message = mockEnabled ?
                                "테스트 모드로 AI 연동이 완료되었습니다. (실제 AI 서버 미연결)" :
                                "실제 AI 서버(8081)와의 연동이 성공적으로 완료되었습니다.";

                        ApiResponseDto2<Object> response = ApiResponseDto2.success(responseData, message);
                        return ResponseEntity.ok(response);
                    })
                    .exceptionally(throwable -> {
                        log.error("AI 서버 연동 실패: requestId={}", requestId, throwable);

                        String errorMessage = determineErrorMessage(throwable, mockEnabled);
                        ApiResponseDto2<Object> errorResponse = ApiResponseDto2.error("500", errorMessage);
                        return ResponseEntity.internalServerError().body(errorResponse);
                    })
                    .orTimeout(timeoutSeconds + 10, java.util.concurrent.TimeUnit.SECONDS);

        } catch (Exception e) {
            log.error("AI 연동 테스트 준비 실패", e);
            ApiResponseDto2<Object> errorResponse = ApiResponseDto2.error(
                    "500", "AI 연동 테스트 준비에 실패했습니다: " + e.getMessage()
            );
            return CompletableFuture.completedFuture(
                    ResponseEntity.internalServerError().body(errorResponse));
        }
    }

    /**
     * AI 서버 연결 상태 종합 체크
     * GET /api/v1/kafka-test/ai-server-status
     */
    @GetMapping("/ai-server-status")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<Map<String, Object>>>> checkAiServerStatus() {
        return aiCommunicationService.checkKafkaConnection()
                .thenApply(isConnected -> {
                    Map<String, Object> statusInfo = Map.of(
                            "kafka_connected", isConnected,
                            "backend_server", "localhost:8080",
                            "ai_server", "localhost:8081",
                            "mock_mode", mockEnabled,
                            "timeout_seconds", timeoutSeconds,
                            "pending_requests", aiCommunicationService.getPendingRequestCount(),
                            "topics", Map.of(
                                    "request_topic", "ai-report-request",
                                    "response_topic", "ai-report-response"
                            ),
                            "system_status", isConnected ?
                                    (mockEnabled ? "READY_FOR_TEST" : "READY_FOR_PRODUCTION") :
                                    "KAFKA_CONNECTION_FAILED",
                            "timestamp", java.time.LocalDateTime.now()
                    );

                    String message;
                    if (!isConnected) {
                        message = "Kafka 연결에 문제가 있습니다. Kafka 서버 상태를 확인해주세요.";
                    } else if (mockEnabled) {
                        message = "테스트 모드입니다. 실제 AI 서버(8081) 없이도 동작합니다.";
                    } else {
                        message = "실제 AI 서버(8081) 연동 준비가 완료되었습니다.";
                    }

                    ApiResponseDto2<Map<String, Object>> response = ApiResponseDto2.success(statusInfo, message);
                    return ResponseEntity.ok(response);
                })
                .exceptionally(throwable -> {
                    log.error("AI 서버 상태 확인 실패", throwable);

                    Map<String, Object> errorInfo = Map.of(
                            "kafka_connected", false,
                            "error_message", throwable.getMessage(),
                            "timestamp", java.time.LocalDateTime.now(),
                            "system_status", "ERROR"
                    );

                    ApiResponseDto2<Map<String, Object>> errorResponse = ApiResponseDto2.error(
                            "500", "AI 서버 상태 확인에 실패했습니다: " + throwable.getMessage()
                    );
                    return ResponseEntity.internalServerError().body(errorResponse);
                });
    }

    /**
     * 실제 vs Mock 모드 전환 테스트
     * POST /api/v1/kafka-test/switch-mode
     */
    @PostMapping("/switch-mode")
    public ResponseEntity<ApiResponseDto2<Object>> testModeSwitching() {
        try {
            Map<String, Object> modeInfo = Map.of(
                    "current_mode", mockEnabled ? "MOCK" : "REAL_AI",
                    "backend_port", 8080,
                    "ai_server_port", 8081,
                    "recommendation", mockEnabled ?
                            "현재 Mock 모드입니다. 실제 AI 서버 테스트를 위해서는 application.yml에서 ai.mock.enabled=false로 설정하고 재시작하세요." :
                            "현재 실제 AI 모드입니다. AI 서버(8081)가 실행되고 있는지 확인하세요.",
                    "mock_enabled", mockEnabled,
                    "timeout_seconds", timeoutSeconds,
                    "status_check_endpoint", "/api/v1/kafka-test/ai-server-status",
                    "test_endpoint", "/api/v1/kafka-test/test-with-company-data"
            );

            ApiResponseDto2<Object> response = ApiResponseDto2.success(
                    modeInfo,
                    "현재 AI 연동 모드 정보를 확인했습니다."
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("모드 정보 조회 실패", e);
            ApiResponseDto2<Object> errorResponse = ApiResponseDto2.error(
                    "500", "모드 정보 조회에 실패했습니다: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // ========== 기존 메서드들 (개선된 버전) ==========

    @PostMapping("/send")
    public ResponseEntity<ApiResponseDto2<String>> sendTestMessage(
            @RequestBody(required = false) Map<String, String> request) {
        try {
            if (request == null) {
                request = Map.of(
                        "topic", "test-topic",
                        "message", "Hello Kafka!",
                        "key", "test-key"
                );
            }

            String topic = request.getOrDefault("topic", "test-topic");
            String message = request.getOrDefault("message", "Hello Kafka!");
            String key = request.getOrDefault("key", "test-key");

            log.info("Kafka 테스트 메시지 전송: topic={}, key={}, message={}", topic, key, message);

            kafkaTemplate.send(topic, key, message);

            ApiResponseDto2<String> response = ApiResponseDto2.success(
                    "메시지 전송 완료",
                    "Kafka 테스트 메시지가 성공적으로 전송되었습니다."
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Kafka 테스트 메시지 전송 실패", e);
            ApiResponseDto2<String> errorResponse = ApiResponseDto2.error(
                    "500", "Kafka 메시지 전송에 실패했습니다: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/full-flow")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<Object>>> testFullKafkaFlow() {
        log.info("Kafka 전체 플로우 테스트 시작 (Mock모드: {})", mockEnabled);

        try {
            CompanyDataDto2 testCompanyData = CompanyDataDto2.builder()
                    .corpName("카프카 테스트 주식회사")
                    .corpNameEng("Kafka Test Corp Ltd.")
                    .ceoName("김카프카")
                    .address("서울특별시 서초구 카프카로 100")
                    .phoneNo("02-9999-8888")
                    .homeUrl("https://kafkatest.com")
                    .stockCode("KAFKA1")
                    .industyCode("73210")
                    .build();

            // testWithCompanyData 메서드 재사용
            return testWithCompanyData(testCompanyData);

        } catch (Exception e) {
            log.error("Kafka 전체 플로우 테스트 준비 실패", e);
            ApiResponseDto2<Object> errorResponse = ApiResponseDto2.error(
                    "500", "Kafka 전체 플로우 테스트 준비에 실패했습니다: " + e.getMessage()
            );
            return CompletableFuture.completedFuture(
                    ResponseEntity.internalServerError().body(errorResponse));
        }
    }

    @GetMapping("/health-sync")
    public ResponseEntity<ApiResponseDto2<Map<String, Object>>> checkKafkaHealthSync() {
        try {
            boolean isConnected = aiCommunicationService.testKafkaConnectionSync();

            Map<String, Object> healthInfo = Map.of(
                    "kafka_connected", isConnected,
                    "backend_port", 8080,
                    "ai_server_port", 8081,
                    "mock_mode", mockEnabled,
                    "timeout_seconds", timeoutSeconds,
                    "pending_requests", aiCommunicationService.getPendingRequestCount(),
                    "timestamp", java.time.LocalDateTime.now(),
                    "status", isConnected ? "HEALTHY" : "UNHEALTHY",
                    "test_type", "SYNC"
            );

            String message = isConnected ?
                    "Kafka 연결 상태가 정상입니다." :
                    "Kafka 연결에 문제가 있습니다.";

            ApiResponseDto2<Map<String, Object>> response = ApiResponseDto2.success(healthInfo, message);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("동기 Kafka 상태 확인 실패", e);
            ApiResponseDto2<Map<String, Object>> errorResponse = ApiResponseDto2.error(
                    "500", "Kafka 상태 확인에 실패했습니다: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponseDto2<Map<String, Object>>> getPendingRequests() {
        try {
            int pendingCount = aiCommunicationService.getPendingRequestCount();

            Map<String, Object> info = Map.of(
                    "pending_count", pendingCount,
                    "backend_port", 8080,
                    "ai_server_port", 8081,
                    "mock_mode", mockEnabled,
                    "timeout_seconds", timeoutSeconds,
                    "timestamp", java.time.LocalDateTime.now(),
                    "message", pendingCount > 0 ?
                            pendingCount + "개의 요청이 처리 대기 중입니다." :
                            "대기 중인 요청이 없습니다."
            );

            ApiResponseDto2<Map<String, Object>> response = ApiResponseDto2.success(
                    info, "대기 중인 요청 정보를 조회했습니다."
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("대기 중인 요청 조회 실패", e);
            ApiResponseDto2<Map<String, Object>> errorResponse = ApiResponseDto2.error(
                    "500", "대기 중인 요청 조회에 실패했습니다."
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // ========== 유틸리티 메서드들 ==========

    private String truncateString(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str != null ? str : "내용 없음";
        }
        return str.substring(0, maxLength) + "... (총 " + str.length() + "자)";
    }

    private String determineErrorMessage(Throwable throwable, boolean mockEnabled) {
        if (throwable.getCause() instanceof java.util.concurrent.TimeoutException) {
            if (mockEnabled) {
                return "Mock AI 처리 시간이 초과되었습니다. 시스템 상태를 확인해주세요.";
            } else {
                return "AI 서버(8081) 응답 시간이 초과되었습니다. AI 서버가 실행 중인지, Kafka 연결이 정상인지 확인해주세요.";
            }
        } else if (throwable.getMessage() != null && throwable.getMessage().contains("kafka")) {
            return "Kafka 연결에 문제가 있습니다. Kafka 서버(기본:9092) 상태를 확인해주세요.";
        } else {
            String baseMessage = mockEnabled ?
                    "Mock AI 처리에 실패했습니다" :
                    "실제 AI 서버(8081) 연동에 실패했습니다";
            return baseMessage + ": " + throwable.getMessage();
        }
    }
}