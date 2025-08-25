package com.example.finalproject.apitest.dto.periodic.response;

// 최대주주 현황
import com.example.finalproject.apitest.entity.periodic.DartMajorShareholderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
//
@Getter
@Builder
public class DartMajorShareholderStatusResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 법인명
    private String nm; // 성명
    private String relate; // 관계
    private String stockKnd; // 주식 종류
    private Long bsisPosesnStockCo; // 기초 소유 주식 수
    private Double bsisPosesnStockQotaRt; // 기초 소유 주식 지분 율
    private Long trmendPosesnStockCo; // 기말 소유 주식 수
    private Double trmendPosesnStockQotaRt; // 기말 소유 주식 지분 율
    private String rm; // 비고
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartMajorShareholderStatusResponse from(DartMajorShareholderStatus entity) {
        return DartMajorShareholderStatusResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .nm(entity.getNm())
                .relate(entity.getRelate())
                .stockKnd(entity.getStockKnd())
                .bsisPosesnStockCo(entity.getBsisPosesnStockCo())
                .bsisPosesnStockQotaRt(entity.getBsisPosesnStockQotaRt())
                .trmendPosesnStockCo(entity.getTrmendPosesnStockCo())
                .trmendPosesnStockQotaRt(entity.getTrmendPosesnStockQotaRt())
                .rm(entity.getRm())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}