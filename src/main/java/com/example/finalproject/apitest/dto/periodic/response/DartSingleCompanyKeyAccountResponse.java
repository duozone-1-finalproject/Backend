package com.example.finalproject.apitest.dto.periodic.response;

// 단일회사 주요계정
import com.example.finalproject.apitest.entity.periodic.DartSingleCompanyKeyAccount;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DartSingleCompanyKeyAccountResponse {

    private String rceptNo; // 접수번호
    private String bsnsYear; // 사업 연도
    private String stockCode; // 종목 코드
    private String reprtCode; // 보고서 코드
    private String accountNm; // 계정명
    private String fsDiv; // 재무제표구분
    private String fsNm; // 재무제표명
    private String sjDiv; // 재무제표구분
    private String sjNm; // 재무제표명
    private String thstrmNm; // 당기명
    private String thstrmDt; // 당기일자
    private Long thstrmAmount; // 당기금액
    private Long thstrmAddAmount; // 당기누적금액
    private String frmtrmNm; // 전기명
    private String frmtrmDt; // 전기일자
    private Long frmtrmAmount; // 전기금액
    private Long frmtrmAddAmount; // 전기누적금액
    private String bfefrmtrmNm; // 전전기명
    private String bfefrmtrmDt; // 전전기일자
    private Long bfefrmtrmAmount; // 전전기금액
    private Integer ord; // 계정과목 정렬순서
    private String currency; // 통화 단위

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartSingleCompanyKeyAccountResponse from(DartSingleCompanyKeyAccount entity) {
        return DartSingleCompanyKeyAccountResponse.builder()
                .rceptNo(entity.getRceptNo())
                .bsnsYear(entity.getBsnsYear())
                .stockCode(entity.getStockCode())
                .reprtCode(entity.getReprtCode())
                .accountNm(entity.getAccountNm())
                .fsDiv(entity.getFsDiv())
                .fsNm(entity.getFsNm())
                .sjDiv(entity.getSjDiv())
                .sjNm(entity.getSjNm())
                .thstrmNm(entity.getThstrmNm())
                .thstrmDt(entity.getThstrmDt())
                .thstrmAmount(entity.getThstrmAmount())
                .thstrmAddAmount(entity.getThstrmAddAmount())
                .frmtrmNm(entity.getFrmtrmNm())
                .frmtrmDt(entity.getFrmtrmDt())
                .frmtrmAmount(entity.getFrmtrmAmount())
                .frmtrmAddAmount(entity.getFrmtrmAddAmount())
                .bfefrmtrmNm(entity.getBfefrmtrmNm())
                .bfefrmtrmDt(entity.getBfefrmtrmDt())
                .bfefrmtrmAmount(entity.getBfefrmtrmAmount())
                .ord(entity.getOrd())
                .currency(entity.getCurrency())
                .build();
    }
}