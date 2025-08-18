package com.example.apitest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DartUnderwriterInfoDto {
    @JsonProperty("rcept_no")
    private String rceptNo;

    @JsonProperty("corp_cls")
    private String corpCls;

    @JsonProperty("corp_code")
    private String corpCode;

    @JsonProperty("corp_name")
    private String corpName;

    @JsonProperty("actsen")
    private String actsen;

    @JsonProperty("actnmn")
    private String actnmn;

    @JsonProperty("stksen")
    private String stksen;

    @JsonProperty("udtcnt")
    private String udtcnt;

    @JsonProperty("udtamt")
    private String udtamt;

    @JsonProperty("udtprc")
    private String udtprc;

    @JsonProperty("udtmth")
    private String udtmth;
}
