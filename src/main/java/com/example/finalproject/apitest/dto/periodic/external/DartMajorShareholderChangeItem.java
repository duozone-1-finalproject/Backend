package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartMajorShareholderChangeItem {

    @JsonProperty("rcept_no")
    private String rceptNo;

    @JsonProperty("corp_cls")
    private String corpCls;

    @JsonProperty("corp_code")
    private String corpCode;

    @JsonProperty("corp_name")
    private String corpName;

    @JsonProperty("change_on")
    private String changeOn;

    @JsonProperty("mxmm_shrhldr_nm")
    private String mxmmShrhldrNm;

    @JsonProperty("posesn_stock_co")
    private String posesnStockCo;

    @JsonProperty("qota_rt")
    private String qotaRt;

    @JsonProperty("change_cause")
    private String changeCause;

    @JsonProperty("rm")
    private String rm;

    @JsonProperty("stlm_dt")
    private String stlmDt;
}