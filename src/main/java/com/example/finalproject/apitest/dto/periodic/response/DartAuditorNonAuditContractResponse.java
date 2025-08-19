package com.example.finalproject.apitest.dto.periodic.response;

// 회계감사인과의 비감사용역 계약체결 현황
import com.example.finalproject.apitest.entity.periodic.DartNonAuditServiceContract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartAuditorNonAuditContractResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String bsnsYear; // 사업연도
    private LocalDate cntrctCnclsDe; // 계약체결일
    private String servcCn; // 용역내용
    private String servcExcPd; // 용역수행기간
    private String servcMendng; // 용역보수
    private String rm; // 비고
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartNonAuditServiceContractResponse from(DartNonAuditServiceContract entity) {
        return DartNonAuditServiceContractResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .bsnsYear(entity.getBsnsYear())
                .cntrctCnclsDe(entity.getCntrctCnclsDe())
                .servcCn(entity.getServcCn())
                .servcExcPd(entity.getServcExcPd())
                .servcMendng(entity.getServcMendng())
                .rm(entity.getRm())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}