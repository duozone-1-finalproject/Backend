package com.example.finalproject.apitest.dto.periodic.response;

// 감사용역체결현황
import com.example.finalproject.apitest.entity.periodic.DartAuditServiceContract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartAuditServiceContractResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String bsnsYear; // 사업연도
    private String adtor; // 감사인
    private String cn; // 내용
    private String mendng; // 보수 (구)
    private String totReqreTime; // 총소요시간 (구)
    private String adtCntrctDtlsMendng; // 감사계약내역(보수) (신)
    private String adtCntrctDtlsTime; // 감사계약내역(시간) (신)
    private String realExcDtlsMendng; // 실제수행내역(보수) (신)
    private String realExcDtlsTime; // 실제수행내역(시간) (신)
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartAuditServiceContractResponse from(DartAuditServiceContract entity) {
        return DartAuditServiceContractResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .bsnsYear(entity.getBsnsYear())
                .adtor(entity.getAdtor())
                .cn(entity.getCn())
                .mendng(entity.getMendng())
                .totReqreTime(entity.getTotReqreTime())
                .adtCntrctDtlsMendng(entity.getAdtCntrctDtlsMendng())
                .adtCntrctDtlsTime(entity.getAdtCntrctDtlsTime())
                .realExcDtlsMendng(entity.getRealExcDtlsMendng())
                .realExcDtlsTime(entity.getRealExcDtlsTime())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}