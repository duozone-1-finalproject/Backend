// src/main/java/com/example/ai_backend/controller/AiReportController.java
package com.example.finalproject.ai_backend.controller;

import com.example.finalproject.ai_backend.dto.ApiResponseDto2;
import com.example.finalproject.ai_backend.dto.AiResponseDto;
import com.example.finalproject.ai_backend.dto.SearchResultDto;
import com.example.finalproject.ai_backend.dto.ReportGenerationRequestDto;
import com.example.finalproject.ai_backend.service.ReportGenerationService;
import com.example.finalproject.ai_backend.service.OpenSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/ai-reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AiReportController {

    private final ReportGenerationService reportGenerationService;
    private final OpenSearchService openSearchService;

    @GetMapping("/health")
    public ResponseEntity<ApiResponseDto2<String>> healthCheck() {
        log.info("AI 백엔드 헬스체크 요청");
        return ResponseEntity.ok(ApiResponseDto2.success(
                "OK", "AI 백엔드 시스템이 정상적으로 동작하고 있습니다."
        ));
    }

    @PostMapping("/generate")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<AiResponseDto>>> generateReport(
            @Valid @RequestBody ReportGenerationRequestDto request) {
        log.info("보고서 생성 요청 수신: corpCode={}, reportType={}",
                request.getCorpCode(), request.getReportType());

        return reportGenerationService.generateReport(request.getCorpCode(), request.getReportType())
                .thenApply(aiResponse ->
                        ResponseEntity.ok(ApiResponseDto2.success(aiResponse, "보고서가 성공적으로 생성되었습니다."))
                )
                .exceptionally(throwable -> {
                    log.error("보고서 생성 실패", throwable);
                    return ResponseEntity.internalServerError()
                            .body(ApiResponseDto2.error("500", "보고서 생성에 실패했습니다: " + throwable.getMessage()));
                });
    }

    @GetMapping("/status/{requestId}")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<String>>> getReportStatus(
            @PathVariable String requestId) {
        log.info("보고서 상태 확인 요청: {}", requestId);

        return reportGenerationService.getReportStatus(requestId)
                .thenApply(status ->
                        ResponseEntity.ok(ApiResponseDto2.success(status, "보고서 상태를 조회했습니다."))
                )
                .exceptionally(throwable -> {
                    log.error("보고서 상태 조회 실패: {}", requestId, throwable);
                    return ResponseEntity.internalServerError()
                            .body(ApiResponseDto2.error("500", "보고서를 찾을 수 없습니다: " + throwable.getMessage()));
                });
    }

    @GetMapping("/company/{companyName}")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<SearchResultDto<Map<String, Object>>>>> getReportsByCompany(
            @PathVariable String companyName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("회사별 보고서 조회 요청: company={}, page={}, size={}", companyName, page, size);

        // 역할에 맞게 ReportGenerationService가 아닌 OpenSearchService를 호출하도록 변경합니다.
        // 이렇게 하면 ReportGenerationService에서 OpenSearch 관련 코드를 모두 제거할 수 있습니다.
        return openSearchService.searchReportsByCompany(companyName, page, size)
                .thenApply(searchResult ->
                        ResponseEntity.ok(ApiResponseDto2.success(searchResult, companyName + "의 보고서 목록을 조회했습니다."))
                )
                .exceptionally(throwable -> {
                    log.error("회사별 보고서 조회 실패: {}", companyName, throwable);
                    return ResponseEntity.internalServerError()
                            .body(ApiResponseDto2.error("500", "보고서 조회에 실패했습니다: " + throwable.getMessage()));
                });
    }

    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<SearchResultDto<Map<String, Object>>>>> searchReports(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page, // 페이지는 1부터 시작하는 것이 사용자 친화적입니다.
            @RequestParam(defaultValue = "10") int size) {
        log.info("키워드 보고서 검색 요청: keyword={}, page={}, size={}", keyword, page, size);

        return openSearchService.searchReportsByKeyword(keyword, page, size)
                .thenApply(searchResult ->
                        ResponseEntity.ok(ApiResponseDto2.success(
                                searchResult, "키워드 '" + keyword + "'로 보고서를 검색했습니다."))
                )
                .exceptionally(throwable -> {
                    log.error("키워드 검색 실패: {}", keyword, throwable);
                    return ResponseEntity.internalServerError()
                            .body(ApiResponseDto2.error("500", "보고서 검색에 실패했습니다: " + throwable.getMessage()));
                });
    }
}
