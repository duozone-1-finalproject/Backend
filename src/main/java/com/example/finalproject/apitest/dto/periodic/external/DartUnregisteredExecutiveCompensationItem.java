package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartUnregisteredExecutiveCompensationItem {

    @JsonProperty("rcept_no")
    private String rceptNo;
    @JsonProperty("corp_cls")
    private String corpCls;
    @JsonProperty("corp_code")
    private String corpCode;
    @JsonProperty("corp_name")
    private String corpName;
    @JsonProperty("se")
    private String se;
    @JsonProperty("nmpr")
    private String nmpr;
    @JsonProperty("fyer_salary_totamt")
    private String fyerSalaryTotamt;
    @JsonProperty("jan_salary_am")
    private String janSalaryAm;
    @JsonProperty("rm")
    private String rm;
    @JsonProperty("stlm_dt")
    private String stlmDt;
}
