package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartHybridSecuritiesBalanceItem {

    @JsonProperty("rcept_no")
    private String rceptNo;
    @JsonProperty("corp_cls")
    private String corpCls;
    @JsonProperty("corp_code")
    private String corpCode;
    @JsonProperty("corp_name")
    private String corpName;
    @JsonProperty("remndr_exprtn1")
    private String remndrExprtn1;
    @JsonProperty("remndr_exprtn2")
    private String remndrExprtn2;
    @JsonProperty("yy1_below")
    private String yy1Below;
    @JsonProperty("yy1_excess_yy5_below")
    private String yy1ExcessYy5Below;
    @JsonProperty("yy5_excess_yy10_below")
    private String yy5ExcessYy10Below;
    @JsonProperty("yy10_excess_yy15_below")
    private String yy10ExcessYy15Below;
    @JsonProperty("yy15_excess_yy20_below")
    private String yy15ExcessYy20Below;
    @JsonProperty("yy20_excess_yy30_below")
    private String yy20ExcessYy30Below;
    @JsonProperty("yy30_excess")
    private String yy30Excess;
    @JsonProperty("sm")
    private String sm;
    @JsonProperty("stlm_dt")
    private String stlmDt;
}
