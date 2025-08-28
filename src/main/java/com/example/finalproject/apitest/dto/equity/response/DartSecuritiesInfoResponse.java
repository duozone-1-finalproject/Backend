package com.example.finalproject.apitest.dto.equity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DartSecuritiesInfoResponse {
    @JsonProperty("rcept_no")
    private String rceptNo;

    @JsonProperty("corp_cls")
    private String corpCls;

    @JsonProperty("corp_code")
    private String corpCode;

    @JsonProperty("corp_name")
    private String corpName;

    @JsonProperty("stksen")
    private String stksen;

    @JsonProperty("stkcnt")
    private String stkcnt;

    @JsonProperty("fv")
    private String fv;

    @JsonProperty("slprc")
    private String slprc;

    @JsonProperty("slta")
    private String slta;

    @JsonProperty("slmthn")
    private String slmthn;
}
