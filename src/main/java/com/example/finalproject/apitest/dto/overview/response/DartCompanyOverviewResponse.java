package com.example.finalproject.apitest.dto.overview.response;

import com.example.finalproject.apitest.entity.overview.DartCompanyOverview;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartCompanyOverviewResponse {

    private String corpCode;
    private String corpName;
    private String corpNameEng;
    private String stockName;
    private String stockCode;
    private String ceoNm;
    private String corpCls;
    private String jurirNo;
    private String bizrNo;
    private String adres;
    private String hmUrl;
    private String irUrl;
    private String phnNo;
    private String faxNo;
    private String indutyCode;
    private LocalDate estDt;
    private String accMt;

    public static DartCompanyOverviewResponse from(DartCompanyOverview entity) {
        return DartCompanyOverviewResponse.builder()
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .corpNameEng(entity.getCorpNameEng())
                .stockName(entity.getStockName())
                .stockCode(entity.getStockCode())
                .ceoNm(entity.getCeoNm())
                .corpCls(entity.getCorpCls())
                .jurirNo(entity.getJurirNo())
                .bizrNo(entity.getBizrNo())
                .adres(entity.getAdres())
                .hmUrl(entity.getHmUrl())
                .irUrl(entity.getIrUrl())
                .phnNo(entity.getPhnNo())
                .faxNo(entity.getFaxNo())
                .indutyCode(entity.getIndutyCode())
                .estDt(entity.getEstDt())
                .accMt(entity.getAccMt())
                .build();
    }
}

