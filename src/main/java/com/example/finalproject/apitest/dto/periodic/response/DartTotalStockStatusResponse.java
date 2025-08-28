package com.example.finalproject.apitest.dto.periodic.response;

// 주식의 총수 현황
import com.example.finalproject.apitest.entity.periodic.DartTotalStockStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartTotalStockStatusResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String se; // 구분
    private Long isuStockTotqy; // 발행할 주식의 총수
    private Long nowToIsuStockTotqy; // 현재까지 발행한 주식의 총수
    private Long nowToDcrsStockTotqy; // 현재까지 감소한 주식의 총수
    private Long redc; // 감자
    private Long profitIncnr; // 이익소각
    private Long rdmstkRepy; // 상환주식의 상환
    private Long etc; // 기타
    private Long istcTotqy; // 발행주식의 총수
    private Long tesstkCo; // 자기주식수
    private Long distbStockCo; // 유통주식수
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartTotalStockStatusResponse from(DartTotalStockStatus entity) {
        return DartTotalStockStatusResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .se(entity.getSe())
                .isuStockTotqy(entity.getIsuStockTotqy())
                .nowToIsuStockTotqy(entity.getNowToIsuStockTotqy())
                .nowToDcrsStockTotqy(entity.getNowToDcrsStockTotqy())
                .redc(entity.getRedc())
                .profitIncnr(entity.getProfitIncnr())
                .rdmstkRepy(entity.getRdmstkRepy())
                .etc(entity.getEtc())
                .istcTotqy(entity.getIstcTotqy())
                .tesstkCo(entity.getTesstkCo())
                .distbStockCo(entity.getDistbStockCo())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}