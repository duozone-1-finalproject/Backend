// src/main/java/com/example/ai_backend/service/CompanyDataService.java
package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.CompanyDataDto2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class CompanyDataService2 {

    private final WebClient webClient;

    @Value("${dart.api.key}")
    private String dartApiKey;

    @Value("${dart.api.base-url}")
    private String dartBaseUrl;

    public CompanyDataService2(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(dartBaseUrl).build();
    }

    /**
     * DART API를 통해 회사 정보 조회
     */
    public CompletableFuture<CompanyDataDto2> getCompanyDataFromDart(String corpCode) {
        log.info("DART API에서 회사 정보 조회 시작: {}", corpCode);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/company.json")
                        .queryParam("crtfc_key", dartApiKey)
                        .queryParam("corp_code", corpCode)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::mapToCompanyDataDto)
                .doOnSuccess(dto -> log.info("DART API 조회 완료: {}", dto.getCorpName()))
                .doOnError(error -> log.error("DART API 조회 실패: {}", error.getMessage()))
                .toFuture();
    }

    /**
     * DB에서 회사 정보 조회 (실제 구현시 JPA Repository 사용)
     */
    public CompletableFuture<CompanyDataDto2> getCompanyDataFromDb(String corpCode) {
        log.info("DB에서 회사 정보 조회 시작: {}", corpCode);

        // 임시 데이터 (실제 구현시 DB 조회로 대체)
        CompanyDataDto2 mockData = CompanyDataDto2.builder()
                .corpName("오픈엣지테크놀로지 주식회사")
                .corpNameEng("OpenEdge Technology Co., Ltd.")
                .stockName("오픈엣지테크놀로지")
                .stockCode("123456")
                .ceoName("김대표")
                .corpClass("K")
                .jurirNo("110111-1234567")
                .bizrNo("123-45-67890")
                .address("서울특별시 강남구 역삼로 114 현죽빌딩 13층")
                .homeUrl("https://openedge.tech")
                .irUrl("https://ir.openedge.tech")
                .phoneNo("02-1234-5678")
                .faxNo("02-1234-5679")
                .industyCode("73210")
                .establishmentDate("20200101")
                .accountMonth("12")
                .build();

        return CompletableFuture.completedFuture(mockData);
    }

    /**
     * 회사 코드로 우선 DB 조회, 없으면 DART API 조회
     */
    public CompletableFuture<CompanyDataDto2> getCompanyData(String corpCode) {
        return getCompanyDataFromDb(corpCode)
                .thenCompose(dbData -> {
                    if (dbData != null && dbData.getCorpName() != null) {
                        log.info("DB에서 회사 정보 조회 성공: {}", dbData.getCorpName());
                        return CompletableFuture.completedFuture(dbData);
                    } else {
                        log.info("DB에 정보 없음, DART API 조회 시도");
                        return getCompanyDataFromDart(corpCode);
                    }
                });
    }

    /**
     * DART API 응답을 CompanyDataDto로 변환
     */
    private CompanyDataDto2 mapToCompanyDataDto(Map<String, Object> response) {
        // DART API 응답 구조에 맞춰 매핑
        Map<String, Object> result = (Map<String, Object>) response.get("result");

        if (result == null) {
            throw new RuntimeException("DART API 응답에서 결과 데이터를 찾을 수 없습니다.");
        }

        return CompanyDataDto2.builder()
                .corpName((String) result.get("corp_name"))
                .corpNameEng((String) result.get("corp_name_eng"))
                .stockName((String) result.get("stock_name"))
                .stockCode((String) result.get("stock_code"))
                .ceoName((String) result.get("ceo_nm"))
                .corpClass((String) result.get("corp_cls"))
                .jurirNo((String) result.get("jurir_no"))
                .bizrNo((String) result.get("bizr_no"))
                .address((String) result.get("adres"))
                .homeUrl((String) result.get("hm_url"))
                .irUrl((String) result.get("ir_url"))
                .phoneNo((String) result.get("phn_no"))
                .faxNo((String) result.get("fax_no"))
                .industyCode((String) result.get("induty_code"))
                .establishmentDate((String) result.get("est_dt"))
                .accountMonth((String) result.get("acc_mt"))
                .build();
    }
}