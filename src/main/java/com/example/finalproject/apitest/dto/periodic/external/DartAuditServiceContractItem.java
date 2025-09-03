package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartAuditServiceContractItem {

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
    @JsonProperty("adtor")
    private String adtor;
    @JsonProperty("cn")
    private String cn;
    @JsonProperty("mendng")
    private String mendng;
    @JsonProperty("tot_reqre_time")
    private String totReqreTime;
    @JsonProperty("adt_cntrct_dtls_mendng")
    private String adtCntrctDtlsMendng;
    @JsonProperty("adt_cntrct_dtls_time")
    private String adtCntrctDtlsTime;
    @JsonProperty("real_exc_dtls_mendng")
    private String realExcDtlsMendng;
    @JsonProperty("real_exc_dtls_time")
    private String realExcDtlsTime;
    @JsonProperty("stlm_dt")
    private String stlmDt;
}
