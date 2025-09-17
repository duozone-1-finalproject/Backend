package com.example.finalproject.apitest.dto.periodic.response;

// 자기주식 취득 및 처분 현황
import com.example.finalproject.apitest.entity.periodic.DartTreasuryStockStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartTreasuryStockStatusResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String acqsMth1; // 취득방법 대분류
    private String acqsMth2; // 취득방법 중분류
    private String acqsMth3; // 취득방법 소분류
    private String stockKnd; // 주식 종류
    private Long bsisQy; // 기초 수량
    private Long changeQyAcqs; // 변동 수량 취득
    private Long changeQyDsps; // 변동 수량 처분
    private Long changeQyIncnr; // 변동 수량 소각
    private Long trmendQy; // 기말 수량
    private String rm; // 비고
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartTreasuryStockStatusResponse from(DartTreasuryStockStatus entity) {
        return DartTreasuryStockStatusResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .acqsMth1(entity.getAcqsMth1())
                .acqsMth2(entity.getAcqsMth2())
                .acqsMth3(entity.getAcqsMth3())
                .stockKnd(entity.getStockKnd())
                .bsisQy(entity.getBsisQy())
                .changeQyAcqs(entity.getChangeQyAcqs())
                .changeQyDsps(entity.getChangeQyDsps())
                .changeQyIncnr(entity.getChangeQyIncnr())
                .trmendQy(entity.getTrmendQy())
                .rm(entity.getRm())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}