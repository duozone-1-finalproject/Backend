package com.example.finalproject.apitest.dto.periodic.response;
// 소액주주 현황
import com.example.finalproject.apitest.entity.periodic.DartMinorityShareholderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartMinorityShareholderStatusResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 법인명
    private String se; // 구분 (소액주주)
    private Long shrholdrCo; // 주주 수
    private Long shrholdrTotCo; // 전체 주주 수
    private Double shrholdrRate; // 주주 비율
    private Long holdStockCo; // 보유 주식 수
    private Long stockTotCo; // 총발행 주식 수
    private Double holdStockRate; // 보유 주식 비율
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartMinorityShareholderStatusResponse from(DartMinorityShareholderStatus entity) {
        return DartMinorityShareholderStatusResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .se(entity.getSe())
                .shrholdrCo(entity.getShrholdrCo())
                .shrholdrTotCo(entity.getShrholdrTotCo())
                .shrholdrRate(entity.getShrholdrRate())
                .holdStockCo(entity.getHoldStockCo())
                .stockTotCo(entity.getStockTotCo())
                .holdStockRate(entity.getHoldStockRate())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}
