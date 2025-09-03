package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.AiResponseDto;
import com.example.finalproject.ai_backend.dto.CompanyDataDto2;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenSearchService {

    private final RestHighLevelClient openSearchClient;
    private final ObjectMapper objectMapper;

    private static final String REPORT_INDEX = "ai-generated-reports";
    private static final String COMPANY_INDEX = "company-data";

    /**
     * AI가 생성한 보고서를 OpenSearch에 저장
     */
    public CompletableFuture<String> saveGeneratedReport(AiResponseDto aiResponse, CompanyDataDto2 companyData) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("OpenSearch에 보고서 저장 시작: {}", aiResponse.getRequestId());

                Map<String, Object> document = createReportDocument(aiResponse, companyData);

                log.debug("저장할 문서 크기: {} bytes", objectMapper.writeValueAsString(document).length());

                IndexRequest indexRequest = new IndexRequest(REPORT_INDEX)
                        .id(aiResponse.getRequestId())
                        .source(document, XContentType.JSON);

                IndexResponse indexResponse = openSearchClient.index(indexRequest, RequestOptions.DEFAULT);

                log.info("OpenSearch 보고서 저장 완료: requestId={}, documentId={}, result={}",
                        aiResponse.getRequestId(),
                        indexResponse.getId(),
                        indexResponse.getResult());

                return indexResponse.getId();

            } catch (Exception e) {
                log.error("OpenSearch 보고서 저장 실패: requestId={}, error={}",
                        aiResponse.getRequestId(), e.getMessage(), e);
                throw new RuntimeException("보고서 저장에 실패했습니다: " + e.getMessage(), e);
            }
        });
    }

    /**
     * 회사 데이터를 OpenSearch에 저장
     */
    public CompletableFuture<String> saveCompanyData(CompanyDataDto2 companyData) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("OpenSearch에 회사 데이터 저장 시작: {}", companyData.getCorpName());

                Map<String, Object> document = createCompanyDocument(companyData);

                IndexRequest indexRequest = new IndexRequest(COMPANY_INDEX)
                        .id(companyData.getStockCode())
                        .source(document, XContentType.JSON);

                IndexResponse indexResponse = openSearchClient.index(indexRequest, RequestOptions.DEFAULT);

                log.info("OpenSearch 회사 데이터 저장 완료: company={}, documentId={}, result={}",
                        companyData.getCorpName(),
                        indexResponse.getId(),
                        indexResponse.getResult());

                return indexResponse.getId();

            } catch (Exception e) {
                log.error("OpenSearch 회사 데이터 저장 실패: company={}, error={}",
                        companyData.getCorpName(), e.getMessage(), e);
                throw new RuntimeException("회사 데이터 저장에 실패했습니다: " + e.getMessage(), e);
            }
        });
    }

    /**
     * OpenSearch 연결 상태 확인
     */
    public boolean checkConnection() {
        try {
            var response = openSearchClient.info(RequestOptions.DEFAULT);
            log.info("OpenSearch 연결 확인: cluster={}, version={}",
                    response.getClusterName(), response.getVersion().getNumber());
            return true;
        } catch (Exception e) {
            log.error("OpenSearch 연결 실패: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 특정 인덱스의 문서 수 조회
     */
    public long getDocumentCount(String indexName) {
        try {
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.size(0);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = openSearchClient.search(searchRequest, RequestOptions.DEFAULT);
            long count = searchResponse.getHits().getTotalHits().value;

            log.info("인덱스 '{}' 문서 수: {}", indexName, count);
            return count;

        } catch (Exception e) {
            log.error("문서 수 조회 실패: index={}, error={}", indexName, e.getMessage());
            return -1L;
        }
    }

    /**
     * 회사명으로 보고서 검색
     */
    public CompletableFuture<SearchResponse> searchReportsByCompany(String companyName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("회사명으로 보고서 검색: {}", companyName);

                SearchRequest searchRequest = new SearchRequest(REPORT_INDEX);
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

                searchSourceBuilder.query(
                        QueryBuilders.matchQuery("company_name", companyName)
                );
                searchSourceBuilder.size(50);

                searchRequest.source(searchSourceBuilder);

                SearchResponse searchResponse = openSearchClient.search(searchRequest, RequestOptions.DEFAULT);

                log.info("보고서 검색 완료: {} 건 조회", searchResponse.getHits().getTotalHits().value);

                return searchResponse;

            } catch (Exception e) {
                log.error("보고서 검색 실패: {}", companyName, e);
                throw new RuntimeException("보고서 검색에 실패했습니다.", e);
            }
        });
    }

    /**
     * 키워드로 보고서 검색
     */
    public CompletableFuture<SearchResponse> searchReportsByKeyword(String keyword) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("키워드로 보고서 검색: {}", keyword);

                SearchRequest searchRequest = new SearchRequest(REPORT_INDEX);
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

                searchSourceBuilder.query(
                        QueryBuilders.multiMatchQuery(keyword)
                                .field("summary")
                                .field("company_name")
                                .field("ceo_name")
                );
                searchSourceBuilder.size(50);

                searchRequest.source(searchSourceBuilder);

                SearchResponse searchResponse = openSearchClient.search(searchRequest, RequestOptions.DEFAULT);

                log.info("키워드 검색 완료: {} 건 조회", searchResponse.getHits().getTotalHits().value);

                return searchResponse;

            } catch (Exception e) {
                log.error("키워드 검색 실패: {}", keyword, e);
                throw new RuntimeException("키워드 검색에 실패했습니다.", e);
            }
        });
    }

    /**
     * 보고서 문서 생성
     */
    private Map<String, Object> createReportDocument(AiResponseDto aiResponse, CompanyDataDto2 companyData) {
        Map<String, Object> document = new HashMap<>();

        document.put("request_id", aiResponse.getRequestId());
        document.put("generated_html", aiResponse.getGeneratedHtml() != null ? aiResponse.getGeneratedHtml() : "");
        document.put("summary", aiResponse.getSummary() != null ? aiResponse.getSummary() : "");
        document.put("processing_time", aiResponse.getProcessingTime() != null ? aiResponse.getProcessingTime() : 0L);
        document.put("ai_status", aiResponse.getStatus() != null ? aiResponse.getStatus() : "UNKNOWN");
        document.put("created_at", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // 회사 정보 추가
        document.put("company_name", companyData.getCorpName() != null ? companyData.getCorpName() : "");
        document.put("company_name_eng", companyData.getCorpNameEng() != null ? companyData.getCorpNameEng() : "");
        document.put("stock_name", companyData.getStockName() != null ? companyData.getStockName() : "");
        document.put("stock_code", companyData.getStockCode() != null ? companyData.getStockCode() : "");
        document.put("ceo_name", companyData.getCeoName() != null ? companyData.getCeoName() : "");
        document.put("corp_class", companyData.getCorpClass() != null ? companyData.getCorpClass() : "");
        document.put("address", companyData.getAddress() != null ? companyData.getAddress() : "");
        document.put("industry_code", companyData.getIndustyCode() != null ? companyData.getIndustyCode() : "");

        return document;
    }

    /**
     * 회사 데이터 문서 생성
     */
    private Map<String, Object> createCompanyDocument(CompanyDataDto2 companyData) {
        Map<String, Object> document = new HashMap<>();

        document.put("corp_name", companyData.getCorpName() != null ? companyData.getCorpName() : "");
        document.put("corp_name_eng", companyData.getCorpNameEng() != null ? companyData.getCorpNameEng() : "");
        document.put("stock_name", companyData.getStockName() != null ? companyData.getStockName() : "");
        document.put("stock_code", companyData.getStockCode() != null ? companyData.getStockCode() : "");
        document.put("ceo_name", companyData.getCeoName() != null ? companyData.getCeoName() : "");
        document.put("corp_class", companyData.getCorpClass() != null ? companyData.getCorpClass() : "");
        document.put("jurir_no", companyData.getJurirNo() != null ? companyData.getJurirNo() : "");
        document.put("bizr_no", companyData.getBizrNo() != null ? companyData.getBizrNo() : "");
        document.put("address", companyData.getAddress() != null ? companyData.getAddress() : "");
        document.put("home_url", companyData.getHomeUrl() != null ? companyData.getHomeUrl() : "");
        document.put("ir_url", companyData.getIrUrl() != null ? companyData.getIrUrl() : "");
        document.put("phone_no", companyData.getPhoneNo() != null ? companyData.getPhoneNo() : "");
        document.put("fax_no", companyData.getFaxNo() != null ? companyData.getFaxNo() : "");
        document.put("industry_code", companyData.getIndustyCode() != null ? companyData.getIndustyCode() : "");
        document.put("establishment_date", companyData.getEstablishmentDate() != null ? companyData.getEstablishmentDate() : "");
        document.put("account_month", companyData.getAccountMonth() != null ? companyData.getAccountMonth() : "");
        document.put("updated_at", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return document;
    }
}