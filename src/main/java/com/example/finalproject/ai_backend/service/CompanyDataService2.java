package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.CompanyDataDto2;
import com.example.finalproject.apitest.entity.overview.DartCompanyOverview;
import com.example.finalproject.apitest.repository.overview.DartCompanyOverviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyDataService2 {

    private final DartCompanyOverviewRepository repository;

    /**
     * DB에서 회사 정보 조회
     */
    public CompanyDataDto2 getCompanyDataFromDb(String corpCode) {
        log.info("DB에서 회사 정보 조회 시작: {}", corpCode);

        DartCompanyOverview entity = repository.findById(corpCode)
                .orElseThrow(() -> new RuntimeException("기업개황 없음: " + corpCode));

        return CompanyDataDto2.builder()
                .corpName(entity.getCorpName())
                .corpNameEng(entity.getCorpNameEng())
                .stockName(entity.getStockName())
                .stockCode(entity.getStockCode())
                .ceoName(entity.getCeoNm())
                .corpClass(entity.getCorpCls())
                .jurirNo(entity.getJurirNo())
                .bizrNo(entity.getBizrNo())
                .address(entity.getAdres())
                .homeUrl(entity.getHmUrl())
                .irUrl(entity.getIrUrl())
                .phoneNo(entity.getPhnNo())
                .faxNo(entity.getFaxNo())
                .industyCode(entity.getIndutyCode())
                .establishmentDate(entity.getEstDt() != null ? entity.getEstDt().toString() : null)
                .accountMonth(entity.getAccMt())
                .build();
    }
}
