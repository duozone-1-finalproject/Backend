package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartMinorityShareholderStatusItem {

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

    @JsonProperty("shrholdr_co")
    private String shrholdrCo;

    @JsonProperty("shrholdr_tot_co")
    private String shrholdrTotCo;

    @JsonProperty("shrholdr_rate")
    private String shrholdrRate;

    @JsonProperty("hold_stock_co")
    private String holdStockCo;

    @JsonProperty("stock_tot_co")
    private String stockTotCo;

    @JsonProperty("hold_stock_rate")
    private String holdStockRate;

    @JsonProperty("stlm_dt")
    private String stlmDt;
}