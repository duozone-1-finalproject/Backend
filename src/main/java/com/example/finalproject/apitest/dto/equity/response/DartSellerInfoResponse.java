package com.example.finalproject.apitest.dto.equity.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DartSellerInfoResponse {
    @JsonProperty("rcept_no")
    private String rceptNo;

    @JsonProperty("corp_cls")
    private String corpCls;

    @JsonProperty("corp_code")
    private String corpCode;

    @JsonProperty("corp_name")
    private String corpName;

    @JsonProperty("hdr")
    private String hdr;

    @JsonProperty("rl_cmp")
    private String rlCmp;

    @JsonProperty("bfsl_hdstk")
    private String bfslHdstk;

    @JsonProperty("slstk")
    private String slstk;

    @JsonProperty("atsl_hdstk")
    private String atslHdstk;
}
