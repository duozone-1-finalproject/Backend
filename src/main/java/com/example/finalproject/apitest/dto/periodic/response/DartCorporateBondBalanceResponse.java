package com.example.finalproject.apitest.dto.periodic.response;

// 회사채 미상환 잔액
import com.example.finalproject.apitest.entity.periodic.DartCorporateBondBalance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartCorporateBondBalanceResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String remndrExprtn1; // 잔여만기1
    private String remndrExprtn2; // 잔여만기2
    private Long yy1Below; // 1년 이하
    private Long yy1ExcessYy2Below; // 1년초과 2년이하
    private Long yy2ExcessYy3Below; // 2년초과 3년이하
    private Long yy3ExcessYy4Below; // 3년초과 4년이하
    private Long yy4ExcessYy5Below; // 4년초과 5년이하
    private Long yy5ExcessYy10Below; // 5년초과 10년이하
    private Long yy10Excess; // 10년초과
    private Long sm; // 합계
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartCorporateBondBalanceResponse from(DartCorporateBondBalance entity) {
        return DartCorporateBondBalanceResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .remndrExprtn1(entity.getRemndrExprtn1())
                .remndrExprtn2(entity.getRemndrExprtn2())
                .yy1Below(entity.getYy1Below())
                .yy1ExcessYy2Below(entity.getYy1ExcessYy2Below())
                .yy2ExcessYy3Below(entity.getYy2ExcessYy3Below())
                .yy3ExcessYy4Below(entity.getYy3ExcessYy4Below())
                .yy4ExcessYy5Below(entity.getYy4ExcessYy5Below())
                .yy5ExcessYy10Below(entity.getYy5ExcessYy10Below())
                .yy10Excess(entity.getYy10Excess())
                .sm(entity.getSm())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}