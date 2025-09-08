// src/main/java/com/example/finalproject/ai_backend/service/AllDartDataService.java
package com.example.finalproject.ai_backend.service;

import com.example.finalproject.apitest.dto.common.AllDartDataResponse;
import com.example.finalproject.apitest.dto.overview.response.DartCompanyOverviewResponse;
import com.example.finalproject.apitest.repository.overview.DartCompanyOverviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AllDartDataService {

    private final DartCompanyOverviewRepository companyOverviewRepo;

    /**
     * 기업 전체 데이터를 조합해서 AllDartDataResponse로 반환
     */
    public AllDartDataResponse getAllDartData(String corpCode) {
        return AllDartDataResponse.builder()
                .companyOverview(
                        companyOverviewRepo.findById(corpCode)
                                .map(DartCompanyOverviewResponse::from)
                                .orElse(null)
                )
                // TODO: executiveStatus, auditOpinion, cbIssuance 등도 여기에 추가
                .build();
    }
}
