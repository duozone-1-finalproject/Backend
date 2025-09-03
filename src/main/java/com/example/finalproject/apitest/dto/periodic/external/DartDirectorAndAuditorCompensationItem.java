package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartDirectorAndAuditorCompensationItem {

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
    @JsonProperty("pymnt_totamt")
    private String pymntTotamt;
    @JsonProperty("psn1_avrg_pymntamt")
    private String psn1AvrgPymntamt;
    @JsonProperty("rm")
    private String rm;
    @JsonProperty("stlm_dt")
    private String stlmDt;
}
