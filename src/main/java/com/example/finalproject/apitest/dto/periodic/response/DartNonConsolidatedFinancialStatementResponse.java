package com.example.finalproject.apitest.dto.periodic.response;

// 단일회사 전체 재무제표
import com.example.finalproject.apitest.entity.periodic.DartNonConsolidatedFinancialStatement;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DartNonConsolidatedFinancialStatementResponse {

    private String rceptNo; // 접수번호
    private String reprtCode; // 보고서 코드
    private String bsnsYear; // 사업 연도
    private String corpCode; // 고유번호
    private String sjDiv; // 재무제표구분
    private String sjNm; // 재무제표명
    private String accountId; // 계정ID
    private String accountNm; // 계정명
    private String accountDetail; // 계정상세
    private String thstrmNm; // 당기명
    private Long thstrmAmount; // 당기금액
    private Long thstrmAddAmount; // 당기누적금액
    private String frmtrmNm; // 전기명
    private Long frmtrmAmount; // 전기금액
    private String frmtrmQNm; // 전기명(분/반기)
    private Long frmtrmQAmount; // 전기금액(분/반기)
    private Long frmtrmAddAmount; // 전기누적금액
    private String bfefrmtrmNm; // 전전기명
    private Long bfefrmtrmAmount; // 전전기금액
    private Integer ord; // 계정과목 정렬순서
    private String currency; // 통화 단위

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartNonConsolidatedFinancialStatementResponse from(DartNonConsolidatedFinancialStatement entity) {
        return DartNonConsolidatedFinancialStatementResponse.builder()
                .rceptNo(entity.getRceptNo())
                .reprtCode(entity.getReprtCode())
                .bsnsYear(entity.getBsnsYear())
                .corpCode(entity.getCorpCode())
                .sjDiv(entity.getSjDiv())
                .sjNm(entity.getSjNm())
                .accountId(entity.getAccountId())
                .accountNm(entity.getAccountNm())
                .accountDetail(entity.getAccountDetail())
                .thstrmNm(entity.getThstrmNm())
                .thstrmAmount(entity.getThstrmAmount())
                .thstrmAddAmount(entity.getThstrmAddAmount())
                .frmtrmNm(entity.getFrmtrmNm())
                .frmtrmAmount(entity.getFrmtrmAmount())
                .frmtrmQNm(entity.getFrmtrmQNm())
                .frmtrmQAmount(entity.getFrmtrmQAmount())
                .frmtrmAddAmount(entity.getFrmtrmAddAmount())
                .bfefrmtrmNm(entity.getBfefrmtrmNm())
                .bfefrmtrmAmount(entity.getBfefrmtrmAmount())
                .ord(entity.getOrd())
                .currency(entity.getCurrency())
                .build();
    }
}