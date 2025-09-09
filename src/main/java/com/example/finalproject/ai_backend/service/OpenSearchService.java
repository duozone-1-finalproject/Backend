// src/main/java/com/example/finalproject/ai_backend/service/OpenSearchService.java
package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.AiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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
                // Document 생성
                Map<String, Object> doc = new HashMap<>();
                doc.put("request_id", aiResponse.getRequestId());
                doc.put("generated_html", aiResponse.getGeneratedHtml());
                doc.put("summary", aiResponse.getSummary());
                doc.put("processing_time", aiResponse.getProcessingTime());
                doc.put("status", aiResponse.getStatus());
                doc.put("created_at", Instant.now().toString());

                // Index 요청
                IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                        .index(INDEX_NAME)
                        .id(aiResponse.getRequestId())
                        .document(doc)
                );

                IndexResponse response = client.index(request);

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
    public CompletableFuture<List<Map<String, Object>>> searchReportsByCompany(String companyName, int page, int size) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Match query 생성
                Query matchQuery = Query.of(q -> q
                        .match(m -> m
                                .field("summary")
                                .query(v -> v.stringValue(companyName))  // ✅ String → FieldValue
                        )
                );

                // Search 요청
                SearchRequest searchRequest = SearchRequest.of(s -> s
                        .index(INDEX_NAME)
                        .query(matchQuery)
                        .from((page - 1) * size)
                        .size(size)
                );

                SearchResponse<Map> response = client.search(searchRequest, Map.class);

                List<Map<String, Object>> results = new ArrayList<>();
                response.hits().hits().forEach(hit -> {
                    Map<String, Object> source = hit.source();
                    if (source != null) {
                        results.add(source);
                    }
                });

                log.info("OpenSearch 검색 완료: {} 건 조회", results.size());
                return results;

            } catch (IOException e) {
                log.error("OpenSearch 검색 실패: {}", e.getMessage(), e);
                throw new RuntimeException("OpenSearch 검색 실패: " + e.getMessage(), e);
            }
        });
    }

    /**
     * 키워드로 다중 필드 검색
     */
    public CompletableFuture<SearchResponse<Map>> searchReportsByKeyword(String keyword, int page, int size) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Multi-match query 생성
                Query multiMatchQuery = Query.of(q -> q
                        .multiMatch(m -> m
                                .query(keyword)
                                .fields("summary", "company_name", "ceo_name")
                        )
                );

                // Search 요청
                SearchRequest searchRequest = SearchRequest.of(s -> s
                        .index(INDEX_NAME)
                        .query(multiMatchQuery)
                        .from(page * size)
                        .size(size)
                );

                SearchResponse<Map> response = client.search(searchRequest, Map.class);

                log.info("키워드 검색 완료: {} hits", response.hits().total().value());
                return response;

            } catch (IOException e) {
                log.error("OpenSearch 키워드 검색 실패: {}", e.getMessage(), e);
                throw new RuntimeException("OpenSearch 키워드 검색 실패: " + e.getMessage(), e);
            }
        });
    }

    /**
     * 인덱스 존재 확인
     */
    public CompletableFuture<Boolean> indexExists() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return client.indices().exists(e -> e.index(INDEX_NAME)).value();
            } catch (IOException e) {
                log.error("인덱스 존재 확인 실패: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    /**
     * 인덱스 생성
     */
    public CompletableFuture<Void> createIndex() {
        return CompletableFuture.runAsync(() -> {
            try {
                client.indices().create(c -> c
                        .index(INDEX_NAME)
                        .mappings(m -> m
                                .properties("request_id", p -> p.keyword(k -> k))
                                .properties("generated_html", p -> p.text(t -> t))
                                .properties("summary", p -> p.text(t -> t.analyzer("standard")))
                                .properties("company_name", p -> p.text(t -> t.analyzer("standard")))
                                .properties("ceo_name", p -> p.text(t -> t.analyzer("standard")))
                                .properties("processing_time", p -> p.long_(l -> l))
                                .properties("status", p -> p.keyword(k -> k))
                                .properties("created_at", p -> p.date(d -> d.format("date_time")))
                        )
                );

                log.info("인덱스 생성 완료: {}", INDEX_NAME);

            } catch (IOException e) {
                log.error("인덱스 생성 실패: {}", e.getMessage(), e);
                throw new RuntimeException("인덱스 생성 실패: " + e.getMessage(), e);
            }
        });
    }

    /**
     * 건강 상태 확인
     */
    public CompletableFuture<String> checkHealth() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var healthResponse = client.cluster().health();
                String status = healthResponse.status().jsonValue();
                log.info("OpenSearch 클러스터 상태: {}", status);
                return status;
            } catch (IOException e) {
                log.error("OpenSearch 상태 확인 실패: {}", e.getMessage(), e);
                return "UNKNOWN";
            }
        });
    }
}