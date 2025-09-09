// src/main/java/com/example/finalproject/ai_backend/service/ReportGenerationService.java
package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.AiRequestDto;
import com.example.finalproject.ai_backend.dto.AiResponseDto;
import com.example.finalproject.apitest.dto.common.AllDartDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportGenerationService {

    private final AllDartDataService allDartDataService;
    private final AiCommunicationService aiCommunicationService;
    private final OpenSearchService openSearchService; // ✅ OpenSearch만 사용

    /**
     * 보고서 생성 (DB → Kafka → AI → OpenSearch)
     */
    public CompletableFuture<AiResponseDto> generateReport(String corpCode, String reportType) {
        String requestId = generateRequestId();
        log.info("보고서 생성 시작: requestId={}, corpCode={}, reportType={}", requestId, corpCode, reportType);

        // DB에서 AllDartData 조회
        AllDartDataResponse allData = allDartDataService.getAllDartData(corpCode);

        // AI 요청 DTO 생성
        AiRequestDto aiRequest = AiRequestDto.builder()
                .requestId(requestId)
                .allDartData(allData)
                .build();

        log.info("AI 요청 준비 완료: requestId={}", requestId);

        // AI 응답을 받으면 OpenSearch에 저장
        return aiCommunicationService.requestReportGeneration(aiRequest)
                .thenCompose(aiResponse -> {
                    log.info("AI 응답 수신 완료, OpenSearch 저장 시작: {}", aiResponse.getRequestId());
                    return saveToOpenSearch(aiResponse);
                });
    }

    /**
     * AI 응답을 OpenSearch에 저장하는 메서드
     */
    private CompletableFuture<AiResponseDto> saveToOpenSearch(AiResponseDto aiResponse) {
        return openSearchService.saveGeneratedReport(aiResponse)
                .thenApply(docId -> {
                    log.info("OpenSearch 저장 완료: {}");
                    return aiResponse;
                });
    }

    private String generateRequestId() {
        return "REP_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    public CompletableFuture<String> getReportStatus(String requestId) {
        return CompletableFuture.supplyAsync(() -> "PROCESSING");
    }

    public CompletableFuture<Boolean> cancelReport(String requestId) {
        return CompletableFuture.supplyAsync(() -> aiCommunicationService.cancelRequest(requestId));
    }

    public CompletableFuture<Map<String, Object>> getSystemStatus() {
        return aiCommunicationService.checkKafkaConnection()
                .thenApply(kafkaStatus -> Map.of(
                        "kafka_connection", kafkaStatus,
                        "pending_requests", aiCommunicationService.getPendingRequestCount(),
                        "timestamp", java.time.LocalDateTime.now().toString()
                ));
    }

    /**
     * 회사 이름으로 보고서 조회 (OpenSearch 사용)
     */
    public CompletableFuture<List<Map<String, Object>>> getReportsByCompany(String companyName, int page, int size) {
        log.info("OpenSearch에서 회사별 보고서 조회 요청: company={}, page={}, size={}", companyName, page, size);
        return openSearchService.searchReportsByCompany(companyName, page, size);
    }
}
