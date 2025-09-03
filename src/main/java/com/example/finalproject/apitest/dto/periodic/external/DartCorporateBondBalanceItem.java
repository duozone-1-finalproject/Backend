package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartCorporateBondBalanceItem {

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
    @JsonProperty("yy1_excess_yy2_below")
    private String yy1ExcessYy2Below;
    @JsonProperty("yy2_excess_yy3_below")
    private String yy2ExcessYy3Below;
    @JsonProperty("yy3_excess_yy4_below")
    private String yy3ExcessYy4Below;
    @JsonProperty("yy4_excess_yy5_below")
    private String yy4ExcessYy5Below;
    @JsonProperty("yy5_excess_yy10_below")
    private String yy5ExcessYy10Below;
    @JsonProperty("yy10_excess")
    private String yy10Excess;
    @JsonProperty("sm")
    private String sm;
    @JsonProperty("stlm_dt")
    private String stlmDt;
}
