package com.example.finalproject.apitest.dto.periodic.response;

// 최대주주 변동현황
import com.example.finalproject.apitest.entity.periodic.DartMajorShareholderChange;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartMajorShareholderChangeResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 법인명
    private LocalDate changeOn; // 변동 일
    private String mxmmShrhldrNm; // 최대 주주 명
    private Long posesnStockCo; // 소유 주식 수
    private Double qotaRt; // 지분 율
    private String changeCause; // 변동 원인
    private String rm; // 비고
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartMajorShareholderChangeResponse from(DartMajorShareholderChange entity) {
        return DartMajorShareholderChangeResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .changeOn(entity.getChangeOn())
                .mxmmShrhldrNm(entity.getMxmmShrhldrNm())
                .posesnStockCo(entity.getPosesnStockCo())
                .qotaRt(entity.getQotaRt())
                .changeCause(entity.getChangeCause())
                .rm(entity.getRm())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}