package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartNonAuditServiceContractItem {

    @JsonProperty("rcept_no")
    private String rceptNo;
    @JsonProperty("corp_cls")
    private String corpCls;
    @JsonProperty("corp_code")
    private String corpCode;
    @JsonProperty("corp_name")
    private String corpName;
    @JsonProperty("bsns_year")
    private String bsnsYear;
    @JsonProperty("cntrct_cncls_de")
    private String cntrctCnclsDe;
    @JsonProperty("servc_cn")
    private String servcCn;
    @JsonProperty("servc_exc_pd")
    private String servcExcPd;
    @JsonProperty("servc_mendng")
    private String servcMendng;
    @JsonProperty("rm")
    private String rm;
    @JsonProperty("stlm_dt")
    private String stlmDt;
}
