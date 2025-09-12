package com.example.finalproject.apitest.dto.periodic.response;

// 신종자본증권 미상환 잔액
import com.example.finalproject.apitest.entity.periodic.DartHybridSecuritiesBalance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartHybridSecuritiesBalanceResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String remndrExprtn1; // 잔여만기1
    private String remndrExprtn2; // 잔여만기2
    private Long yy1Below; // 1년 이하
    private Long yy1ExcessYy5Below; // 1년초과 5년이하
    private Long yy5ExcessYy10Below; // 5년초과 10년이하
    private Long yy10ExcessYy15Below; // 10년초과 15년이하
    private Long yy15ExcessYy20Below; // 15년초과 20년이하
    private Long yy20ExcessYy30Below; // 20년초과 30년이하
    private Long yy30Excess; // 30년초과
    private Long sm; // 합계
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartHybridSecuritiesBalanceResponse from(DartHybridSecuritiesBalance entity) {
        return DartHybridSecuritiesBalanceResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .remndrExprtn1(entity.getRemndrExprtn1())
                .remndrExprtn2(entity.getRemndrExprtn2())
                .yy1Below(entity.getYy1Below())
                .yy1ExcessYy5Below(entity.getYy1ExcessYy5Below())
                .yy5ExcessYy10Below(entity.getYy5ExcessYy10Below())
                .yy10ExcessYy15Below(entity.getYy10ExcessYy15Below())
                .yy15ExcessYy20Below(entity.getYy15ExcessYy20Below())
                .yy20ExcessYy30Below(entity.getYy20ExcessYy30Below())
                .yy30Excess(entity.getYy30Excess())
                .sm(entity.getSm())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}