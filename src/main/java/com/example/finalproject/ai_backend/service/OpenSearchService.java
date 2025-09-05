// src/main/java/com/example/finalproject/ai_backend/service/OpenSearchService.java
package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.AiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenSearchService {

    private final RestHighLevelClient client;

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

                IndexRequest request = new IndexRequest(INDEX_NAME)
                        .id(aiResponse.getRequestId())
                        .source(doc, XContentType.JSON);

                IndexResponse response = client.index(request, RequestOptions.DEFAULT);

                log.info("OpenSearch 저장 완료: id={}, result={}", response.getId(), response.getResult());
                return response.getId();

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
                SearchRequest searchRequest = new SearchRequest(INDEX_NAME);

                SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                        .query(QueryBuilders.matchQuery("summary", companyName))
                        .from((page - 1) * size)
                        .size(size);

                searchRequest.source(sourceBuilder);
                SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

                List<Map<String, Object>> results = new ArrayList<>();
                response.getHits().forEach(hit -> results.add(hit.getSourceAsMap()));

                log.info("OpenSearch 검색 완료: {} 건 조회", results.size());
                return results;

            } catch (IOException e) {
                log.error("OpenSearch 검색 실패: {}", e.getMessage(), e);
                throw new RuntimeException("OpenSearch 검색 실패: " + e.getMessage(), e);
            }
        });
    }

    public CompletableFuture<SearchResponse> searchReportsByKeyword(String keyword, int page, int size) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SearchRequest searchRequest = new SearchRequest(INDEX_NAME);

                SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                        .query(QueryBuilders.multiMatchQuery(keyword, "summary", "company_name", "ceo_name"))
                        .from(page * size)
                        .size(size);

                searchRequest.source(sourceBuilder);
                SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

                log.info("키워드 검색 완료: {} hits", response.getHits().getTotalHits().value);
                return response;

            } catch (IOException e) {
                log.error("OpenSearch 키워드 검색 실패: {}", e.getMessage(), e);
                throw new RuntimeException("OpenSearch 키워드 검색 실패: " + e.getMessage(), e);
            }
        });
    }
}
