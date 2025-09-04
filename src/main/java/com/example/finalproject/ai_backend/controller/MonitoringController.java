// src/main/java/com/example/ai_backend/controller/MonitoringController.java
package com.example.finalproject.ai_backend.controller;

import com.example.finalproject.ai_backend.dto.ApiResponseDto2;
import com.example.finalproject.ai_backend.service.ReportGenerationService;
import com.example.finalproject.ai_backend.service.AiCommunicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/monitoring")
@RequiredArgsConstructor
@CrossOrigin(origins = "${frontend.url}")
public class MonitoringController {

    private final ReportGenerationService reportGenerationService;
    private final AiCommunicationService aiCommunicationService;

    /**
     * 전체 시스템 상태 확인
     * GET /api/v1/monitoring/status
     */
    @GetMapping("/status")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<Map<String, Object>>>> getSystemStatus() {
        log.info("전체 시스템 상태 확인 요청");

        return reportGenerationService.getSystemStatus()
                .thenApply(status -> {
                    ApiResponseDto2<Map<String, Object>> response = ApiResponseDto2.success(
                            status,
                            "시스템 상태를 조회했습니다."
                    );
                    return ResponseEntity.ok(response);
                })
                .exceptionally(throwable -> {
                    log.error("시스템 상태 확인 실패", throwable);
                    ApiResponseDto2<Map<String, Object>> errorResponse = ApiResponseDto2.error(
                            "500",
                            "시스템 상태 확인에 실패했습니다: " + throwable.getMessage()
                    );
                    return ResponseEntity.internalServerError().body(errorResponse);
                });
    }

    /**
     * Kafka 연결 상태 확인
     * GET /api/v1/monitoring/kafka
     */
    @GetMapping("/kafka")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<String>>> getKafkaStatus() {
        log.info("Kafka 연결 상태 확인 요청");

        return aiCommunicationService.checkKafkaConnection()
                .thenApply(isConnected -> {
                    String status = isConnected ? "CONNECTED" : "DISCONNECTED";
                    String message = isConnected ?
                            "Kafka 브로커에 정상적으로 연결되어 있습니다." :
                            "Kafka 브로커 연결에 실패했습니다.";

                    ApiResponseDto2<String> response = ApiResponseDto2.success(status, message);
                    return ResponseEntity.ok(response);
                })
                .exceptionally(throwable -> {
                    log.error("Kafka 상태 확인 실패", throwable);
                    ApiResponseDto2<String> errorResponse = ApiResponseDto2.error(
                            "500",
                            "Kafka 상태 확인에 실패했습니다: " + throwable.getMessage()
                    );
                    return ResponseEntity.internalServerError().body(errorResponse);
                });
    }

    /**
     * 대기 중인 AI 요청 수 조회
     * GET /api/v1/monitoring/pending-requests
     */
    @GetMapping("/pending-requests")
    public ResponseEntity<ApiResponseDto2<Integer>> getPendingRequestCount() {
        log.info("대기 중인 AI 요청 수 조회");

        try {
            int pendingCount = aiCommunicationService.getPendingRequestCount();

            ApiResponseDto2<Integer> response = ApiResponseDto2.success(
                    pendingCount,
                    "현재 " + pendingCount + "개의 요청이 처리 대기 중입니다."
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("대기 중인 요청 수 조회 실패", e);
            ApiResponseDto2<Integer> errorResponse = ApiResponseDto2.error(
                    "500",
                    "대기 중인 요청 수 조회에 실패했습니다."
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 특정 요청 취소
     * DELETE /api/v1/monitoring/requests/{requestId}
     */
    @DeleteMapping("/requests/{requestId}")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<Boolean>>> cancelRequest(
            @PathVariable String requestId) {

        log.info("요청 취소: {}", requestId);

        return reportGenerationService.cancelReport(requestId)
                .thenApply(cancelled -> {
                    if (cancelled) {
                        ApiResponseDto2<Boolean> response = ApiResponseDto2.success(
                                true,
                                "요청이 성공적으로 취소되었습니다."
                        );
                        return ResponseEntity.ok(response);
                    } else {
                        ApiResponseDto2<Boolean> response = ApiResponseDto2.success(
                                false,
                                "요청을 취소할 수 없습니다. (이미 완료되었거나 존재하지 않습니다)"
                        );
                        return ResponseEntity.ok(response);
                    }
                })
                .exceptionally(throwable -> {
                    log.error("요청 취소 실패: {}", requestId, throwable);
                    ApiResponseDto2<Boolean> errorResponse = ApiResponseDto2.error(
                            "500",
                            "요청 취소에 실패했습니다: " + throwable.getMessage()
                    );
                    return ResponseEntity.internalServerError().body(errorResponse);
                });
    }

    /**
     * 시스템 메트릭 조회 (간단한 통계)
     * GET /api/v1/monitoring/metrics
     */
    @GetMapping("/metrics")
    public ResponseEntity<ApiResponseDto2<Map<String, Object>>> getMetrics() {
        log.info("시스템 메트릭 조회");

        try {
            // 런타임 정보 수집
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            long maxMemory = runtime.maxMemory();

            Map<String, Object> metrics = Map.of(
                    "memory", Map.of(
                            "total_mb", totalMemory / 1024 / 1024,
                            "used_mb", usedMemory / 1024 / 1024,
                            "free_mb", freeMemory / 1024 / 1024,
                            "max_mb", maxMemory / 1024 / 1024
                    ),
                    "system", Map.of(
                            "processors", runtime.availableProcessors(),
                            "java_version", System.getProperty("java.version"),
                            "uptime_ms", System.currentTimeMillis()
                    ),
                    "pending_ai_requests", aiCommunicationService.getPendingRequestCount()
            );

            ApiResponseDto2<Map<String, Object>> response = ApiResponseDto2.success(
                    metrics,
                    "시스템 메트릭을 조회했습니다."
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("시스템 메트릭 조회 실패", e);
            ApiResponseDto2<Map<String, Object>> errorResponse = ApiResponseDto2.error(
                    "500",
                    "시스템 메트릭 조회에 실패했습니다."
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}