package com.example.finalproject.apitest.service;

import com.example.finalproject.apitest.dto.common.Company;
import com.example.finalproject.apitest.dto.overview.response.CompanyOverviewResponse;
import com.example.finalproject.apitest.dto.common.Headquarters;
import com.example.finalproject.apitest.config.DartApiProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class DartService2 {

    private final RestClient client;
    private final String apiKey;

    public DartService2(DartApiProperties dartApiProperties) {
        this.client = RestClient.builder()
                .baseUrl(dartApiProperties.getBaseUrl())
                .build();
        this.apiKey = dartApiProperties.getKey();
    }

    /** corpCode로 회사 기본정보 조회 -> 프론트 스키마로 변환 */
    public CompanyOverviewResponse getCompanyOverviewByCorpCode(String corpCode) {
        DartCompanyResponse res = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/company.json")
                        .queryParam("crtfc_key", apiKey)
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", "2022")
                        .queryParam("reprt_code", "11014")
                        .build())
                .retrieve()
                .body(DartCompanyResponse.class);

        // 실패 시 빈 값으로 (프론트는 '-'로 표시)
        if (res == null || !"000".equals(res.getStatus())) {
            return new CompanyOverviewResponse(new Company(new Headquarters(null, null, null)));
        }

        String website = normalizeUrl(res.getHmUrl());
        Headquarters hq = new Headquarters(res.getAdres(), res.getPhnNo(), website);
        return new CompanyOverviewResponse(new Company(hq));
    }

    private String normalizeUrl(String hmUrl) {
        if (hmUrl == null || hmUrl.isBlank()) return null;
        String u = hmUrl.trim();
        if (u.startsWith("http://") || u.startsWith("https://")) return u;
        return "https://" + u;
    }
}