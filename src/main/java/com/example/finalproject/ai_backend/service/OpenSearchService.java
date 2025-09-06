// src/main/java/com/example/finalproject/ai_backend/service/OpenSearchService.java
package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.AiResponseDto;
import com.example.finalproject.ai_backend.dto.SearchResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenSearchService {

    private final OpenSearchClient client;

    private static final String INDEX_NAME = "ai_generated_reports";

    /**
     * AI 응답을 OpenSearch에 저장
     */
    public CompletableFuture<String> saveGeneratedReport(AiResponseDto aiResponse) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Object> doc = new HashMap<>();
                doc.put("request_id", aiResponse.getRequestId());
                doc.put("generated_html", aiResponse.getGeneratedHtml());
                doc.put("summary", aiResponse.getSummary());
                doc.put("processing_time", aiResponse.getProcessingTime());
                doc.put("status", aiResponse.getStatus());
                doc.put("created_at", new Date());

                IndexResponse response = client.index(i -> i
                        .index(INDEX_NAME)
                        .id(aiResponse.getRequestId())
                        .document(doc)
                );

                log.info("OpenSearch 저장 완료: id={}, result={}", response.id(), response.result());
                return response.id();

            } catch (IOException e) {
                log.error("OpenSearch 저장 실패: {}", e.getMessage(), e);
                throw new RuntimeException("OpenSearch 저장 실패: " + e.getMessage(), e);
            }
        });
    }

    /**
     * 회사명으로 보고서 검색
     */
    public CompletableFuture<SearchResultDto<Map<String, Object>>> searchReportsByCompany(String companyName, int page, int size) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SearchResponse<Map> response = client.search(s -> s
                                .index(INDEX_NAME)
                                .query(q -> q
                                        .match(t -> t
                                                .field("summary")
                                                .query(FieldValue.of(companyName)) // ✨ 수정된 부분 1
                                        )
                                )
                                .from((page - 1) * size)
                                .size(size),
                        Map.class
                );

                List<Map<String, Object>> results = response.hits().hits().stream()
                        .map(hit -> (Map<String, Object>) hit.source()) // ✨ 수정된 부분 2
                        .collect(Collectors.toList());

                long totalHits = response.hits().total().value();

                log.info("OpenSearch 검색 완료: {} 건 조회", results.size());
                return new SearchResultDto<>(totalHits, results);

            } catch (IOException e) {
                log.error("OpenSearch 검색 실패: {}", e.getMessage(), e);
                throw new RuntimeException("OpenSearch 검색 실패: " + e.getMessage(), e);
            }
        });
    }

    public CompletableFuture<SearchResultDto<Map<String, Object>>> searchReportsByKeyword(String keyword, int page, int size) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SearchResponse<Map> response = client.search(s -> s
                                .index(INDEX_NAME)
                                .query(q -> q
                                        .multiMatch(m -> m
                                                .query(keyword)
                                                .fields("summary", "company_name", "ceo_name")
                                        )
                                )
                                .from((page - 1) * size)
                                .size(size),
                        Map.class
                );

                List<Map<String, Object>> results = response.hits().hits().stream()
                        .map(hit -> (Map<String, Object>) hit.source()) // ✨ 수정된 부분 3
                        .collect(Collectors.toList());

                long totalHits = response.hits().total().value();

                log.info("키워드 검색 완료: {} hits", totalHits);
                return new SearchResultDto<>(totalHits, results);

            } catch (IOException e) {
                log.error("OpenSearch 키워드 검색 실패: {}", e.getMessage(), e);
                throw new RuntimeException("OpenSearch 키워드 검색 실패: " + e.getMessage(), e);
            }
        });
    }
}