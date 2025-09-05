package com.example.finalproject.ai_backend.controller;

import com.example.finalproject.apitest.dto.common.AllDartDataResponse;
import com.example.finalproject.ai_backend.dto.ApiResponseDto2;
import com.example.finalproject.ai_backend.dto.AiRequestDto;
import com.example.finalproject.ai_backend.dto.CompanyDataDto2;
import com.example.finalproject.ai_backend.service.AiCommunicationService;
import com.example.finalproject.ai_backend.service.AllDartDataService;
import com.example.finalproject.ai_backend.service.ReportGenerationService; // ✅ 추가
import com.example.finalproject.ai_backend.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    private final AllDartDataService allDartDataService;
    private final ReportGenerationService reportGenerationService; // ✅ 추가

    @Value("${ai.timeout.seconds:60}")
    private int timeoutSeconds;

    // ========== 실제 AI 서버(8081) 연동 테스트 (ReportGenerationService 사용) ==========

    @PostMapping("/test-with-company-data")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<Object>>> testWithCompanyData(
            @RequestParam String corpCode) {

        log.info("실제 회사 데이터 AI 연동 테스트 시작: corpCode={}", corpCode);

        try {
            // ✅ ReportGenerationService를 사용하여 전체 플로우 실행
            return reportGenerationService.generateReport(corpCode, "FINANCIAL_ANALYSIS")
                    .thenApply(aiResponse -> {
                        log.info("전체 보고서 생성 완료 (AI + OpenSearch 저장): requestId={}, status={}",
                                aiResponse.getRequestId(), aiResponse.getStatus());

                        Map<String, Object> responseData = new HashMap<>();
                        responseData.put("request_id", aiResponse.getRequestId());
                        responseData.put("status", aiResponse.getStatus());
                        responseData.put("processing_time", aiResponse.getProcessingTime());
                        responseData.put("summary", aiResponse.getSummary() != null ? aiResponse.getSummary() : "요약 정보 없음");
                        responseData.put("full_html_length", aiResponse.getGeneratedHtml() != null ? aiResponse.getGeneratedHtml().length() : 0);
                        responseData.put("corp_code", corpCode);
                        responseData.put("test_type", "FULL_PIPELINE_WITH_OPENSEARCH");
                        responseData.put("ai_server_port", 8081);
                        responseData.put("opensearch_saved", true); // ✅ OpenSearch 저장 완료 표시

                        ApiResponseDto2<Object> response = ApiResponseDto2.success(
                                responseData, "전체 파이프라인(AI 생성 + OpenSearch 저장)이 성공적으로 완료되었습니다."
                        );
                        return ResponseEntity.ok(response);
                    })
                    .exceptionally(throwable -> {
                        log.error("전체 파이프라인 실패: corpCode={}", corpCode, throwable);
                        ApiResponseDto2<Object> errorResponse = ApiResponseDto2.error(
                                "500", determineErrorMessage(throwable)
                        );
                        return ResponseEntity.internalServerError().body(errorResponse);
                    })
                    .orTimeout(timeoutSeconds + 10, java.util.concurrent.TimeUnit.SECONDS);

        } catch (Exception e) {
            log.error("보고서 생성 요청 준비 실패", e);
            ApiResponseDto2<Object> errorResponse = ApiResponseDto2.error(
                    "500", "보고서 생성 요청 준비에 실패했습니다: " + e.getMessage()
            );
            return CompletableFuture.completedFuture(
                    ResponseEntity.internalServerError().body(errorResponse));
        }
    }

    // ========== AI만 테스트 (기존 방식) ==========

    @PostMapping("/test-ai-only")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<Object>>> testAiOnly(
            @RequestParam String corpCode) {

        log.info("AI 서버만 테스트 시작: corpCode={}", corpCode);

        try {
            String requestId = "AI_ONLY_" + UUID.randomUUID().toString().substring(0, 8);

            // DB에서 기업 데이터 조회
            AllDartDataResponse allData = allDartDataService.getAllDartData(corpCode);

            // AI 요청 DTO 생성
            AiRequestDto aiRequest = AiRequestDto.builder()
                    .requestId(requestId)
                    .allDartData(allData)
                    .build();

            log.info("AI 서버(8081) 단독 테스트 요청: requestId={}", requestId);

            // ✅ AiCommunicationService 직접 호출 (OpenSearch 저장 안함)
            return aiCommunicationService.requestReportGeneration(aiRequest)
                    .thenApply(aiResponse -> {
                        log.info("AI 서버 단독 테스트 성공: requestId={}, status={}",
                                aiResponse.getRequestId(), aiResponse.getStatus());

                        Map<String, Object> responseData = new HashMap<>();
                        responseData.put("request_id", aiResponse.getRequestId());
                        responseData.put("status", aiResponse.getStatus());
                        responseData.put("processing_time", aiResponse.getProcessingTime());
                        responseData.put("summary", aiResponse.getSummary());
                        responseData.put("full_html_length", aiResponse.getGeneratedHtml() != null ? aiResponse.getGeneratedHtml().length() : 0);
                        responseData.put("corp_code", corpCode);
                        responseData.put("test_type", "AI_SERVER_ONLY");
                        responseData.put("opensearch_saved", false); // ✅ OpenSearch 저장 안함

                        ApiResponseDto2<Object> response = ApiResponseDto2.success(
                                responseData, "AI 서버(8081) 단독 테스트가 성공적으로 완료되었습니다."
                        );
                        return ResponseEntity.ok(response);
                    })
                    .exceptionally(throwable -> {
                        log.error("AI 서버 단독 테스트 실패: requestId={}", requestId, throwable);
                        ApiResponseDto2<Object> errorResponse = ApiResponseDto2.error(
                                "500", determineErrorMessage(throwable)
                        );
                        return ResponseEntity.internalServerError().body(errorResponse);
                    });

        } catch (Exception e) {
            log.error("AI 단독 테스트 준비 실패", e);
            ApiResponseDto2<Object> errorResponse = ApiResponseDto2.error(
                    "500", "AI 단독 테스트 준비에 실패했습니다: " + e.getMessage()
            );
            return CompletableFuture.completedFuture(
                    ResponseEntity.internalServerError().body(errorResponse));
        }
    }

    // ========== Kafka 상태 확인 ==========

    @GetMapping("/ai-server-status")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<Map<String, Object>>>> checkAiServerStatus() {
        return aiCommunicationService.checkKafkaConnection()
                .thenApply(isConnected -> {
                    Map<String, Object> statusInfo = Map.of(
                            "kafka_connected", isConnected,
                            "backend_server", "localhost:8080",
                            "ai_server", "localhost:8081",
                            "timeout_seconds", timeoutSeconds,
                            "pending_requests", aiCommunicationService.getPendingRequestCount(),
                            "topics", Map.of(
                                    "request_topic", "ai-report-request",
                                    "response_topic", "ai-report-response"
                            ),
                            "system_status", isConnected ? "READY_FOR_PRODUCTION" : "KAFKA_CONNECTION_FAILED",
                            "timestamp", java.time.LocalDateTime.now()
                    );

                    String message = isConnected
                            ? "실제 AI 서버(8081) 연동 준비가 완료되었습니다."
                            : "Kafka 연결에 문제가 있습니다. Kafka 서버 상태를 확인해주세요.";

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

    @GetMapping("/pending")
    public ResponseEntity<ApiResponseDto2<Map<String, Object>>> getPendingRequests() {
        try {
            int pendingCount = aiCommunicationService.getPendingRequestCount();

            Map<String, Object> info = Map.of(
                    "pending_count", pendingCount,
                    "backend_port", 8080,
                    "ai_server_port", 8081,
                    "timeout_seconds", timeoutSeconds,
                    "timestamp", java.time.LocalDateTime.now(),
                    "message", pendingCount > 0
                            ? pendingCount + "개의 요청이 처리 대기 중입니다."
                            : "대기 중인 요청이 없습니다."
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

    // ========== 유틸리티 ==========

    private String truncateString(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str != null ? str : "내용 없음";
        }
        return str.substring(0, maxLength) + "... (총 " + str.length() + "자)";
    }

    private String determineErrorMessage(Throwable throwable) {
        if (throwable.getCause() instanceof java.util.concurrent.TimeoutException) {
            return "AI 서버(8081) 응답 시간이 초과되었습니다. Kafka 연결과 AI 서버 실행 상태를 확인해주세요.";
        } else if (throwable.getMessage() != null && throwable.getMessage().contains("kafka")) {
            return "Kafka 연결에 문제가 있습니다. Kafka 서버 상태를 확인해주세요.";
        } else if (throwable.getMessage() != null && throwable.getMessage().contains("OpenSearch")) {
            return "OpenSearch 저장에 실패했습니다: " + throwable.getMessage();
        } else {
            return "AI 서버(8081) 연동에 실패했습니다: " + throwable.getMessage();
        }
    }
}