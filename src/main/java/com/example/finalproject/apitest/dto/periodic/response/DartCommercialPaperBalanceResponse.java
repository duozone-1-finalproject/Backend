package com.example.finalproject.apitest.dto.periodic.response;

// 기업어음증권 미상환 잔액
import com.example.finalproject.apitest.entity.periodic.DartCommercialPaperBalance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartCommercialPaperBalanceResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String remndrExprtn1; // 잔여만기1
    private String remndrExprtn2; // 잔여만기2
    private Long de10Below; // 10일 이하
    private Long de10ExcessDe30Below; // 10일초과 30일이하
    private Long de30ExcessDe90Below; // 30일초과 90일이하
    private Long de90ExcessDe180Below; // 90일초과 180일이하
    private Long de180ExcessYy1Below; // 180일초과 1년이하
    private Long yy1ExcessYy2Below; // 1년초과 2년이하
    private Long yy2ExcessYy3Below; // 2년초과 3년이하
    private Long yy3Excess; // 3년초과
    private Long sm; // 합계
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartCommercialPaperBalanceResponse from(DartCommercialPaperBalance entity) {
        return DartCommercialPaperBalanceResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .remndrExprtn1(entity.getRemndrExprtn1())
                .remndrExprtn2(entity.getRemndrExprtn2())
                .de10Below(entity.getDe10Below())
                .de10ExcessDe30Below(entity.getDe10ExcessDe30Below())
                .de30ExcessDe90Below(entity.getDe30ExcessDe90Below())
                .de90ExcessDe180Below(entity.getDe90ExcessDe180Below())
                .de180ExcessYy1Below(entity.getDe180ExcessYy1Below())
                .yy1ExcessYy2Below(entity.getYy1ExcessYy2Below())
                .yy2ExcessYy3Below(entity.getYy2ExcessYy3Below())
                .yy3Excess(entity.getYy3Excess())
                .sm(entity.getSm())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}