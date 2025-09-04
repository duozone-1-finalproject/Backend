package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartEmployeeStatusItem {

    @JsonProperty("rcept_no")
    private String rceptNo;

    @JsonProperty("corp_cls")
    private String corpCls;

    @JsonProperty("corp_code")
    private String corpCode;

    @JsonProperty("corp_name")
    private String corpName;

    @JsonProperty("fo_bbm")
    private String foBbm;

    @JsonProperty("sexdstn")
    private String sexdstn;

    @JsonProperty("reform_bfe_emp_co_rgllb")
    private String reformBfeEmpCoRgllb;

    @JsonProperty("reform_bfe_emp_co_cnttk")
    private String reformBfeEmpCoCnttk;

    @JsonProperty("reform_bfe_emp_co_etc")
    private String reformBfeEmpCoEtc;

    @JsonProperty("rgllbr_co")
    private String rgllbrCo;

    @JsonProperty("rgllbr_abacpt_labrr_co")
    private String rgllbrAbacptLabrrCo;

    @JsonProperty("cnttk_co")
    private String cnttkCo;

    @JsonProperty("cnttk_abacpt_labrr_co")
    private String cnttkAbacptLabrrCo;

    @JsonProperty("sm")
    private String sm;

    @JsonProperty("avrg_cnwk_sdytrn")
    private String avrgCnwkSdytrn;

    @JsonProperty("fyer_salary_totamt")
    private String fyerSalaryTotamt;

    @JsonProperty("jan_salary_am")
    private String janSalaryAm;

    @JsonProperty("rm")
    private String rm;

    @JsonProperty("stlm_dt")
    private String stlmDt;
}
