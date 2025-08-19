package com.example.finalproject.apitest.dto.equity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DartRedemptionRightsResponse {
    @JsonProperty("rcept_no")
    private String rceptNo;

    @JsonProperty("corp_cls")
    private String corpCls;

    @JsonProperty("corp_code")
    private String corpCode;

    @JsonProperty("corp_name")
    private String corpName;

    @JsonProperty("grtrs")
    private String grtrs;

    @JsonProperty("exavivr")
    private String exavivr;

    @JsonProperty("grtcnt")
    private String grtcnt;

    @JsonProperty("expd")
    private String expd;

    @JsonProperty("exprc")
    private String exprc;
}
