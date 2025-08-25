package com.example.finalproject.apitest.dto.periodic.response;
// 직원 현황
import com.example.finalproject.apitest.entity.periodic.DartEmployeeStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartEmployeeStatusResponse {

    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private String foBbm;
    private String sexdstn;
    private Long reformBfeEmpCoRgllb;
    private Long reformBfeEmpCoCnttk;
    private Long reformBfeEmpCoEtc;
    private Long rgllbrCo;
    private Long rgllbrAbacptLabrrCo;
    private Long cnttkCo;
    private Long cnttkAbacptLabrrCo;
    private Long sm;
    private String avrgCnwkSdytrn;
    private Long fyerSalaryTotamt;
    private Long janSalaryAm;
    private String rm;
    private LocalDate stlmDt;

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartEmployeeStatusResponse from(DartEmployeeStatus entity) {
        return DartEmployeeStatusResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .foBbm(entity.getFoBbm())
                .sexdstn(entity.getSexdstn())
                .reformBfeEmpCoRgllb(entity.getReformBfeEmpCoRgllb())
                .reformBfeEmpCoCnttk(entity.getReformBfeEmpCoCnttk())
                .reformBfeEmpCoEtc(entity.getReformBfeEmpCoEtc())
                .rgllbrCo(entity.getRgllbrCo())
                .rgllbrAbacptLabrrCo(entity.getRgllbrAbacptLabrrCo())
                .cnttkCo(entity.getCnttkCo())
                .cnttkAbacptLabrrCo(entity.getCnttkAbacptLabrrCo())
                .sm(entity.getSm())
                .avrgCnwkSdytrn(entity.getAvrgCnwkSdytrn())
                .fyerSalaryTotamt(entity.getFyerSalaryTotamt())
                .janSalaryAm(entity.getJanSalaryAm())
                .rm(entity.getRm())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}