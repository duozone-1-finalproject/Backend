package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartShortTermBondBalanceItem {

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
    @JsonProperty("de10_below")
    private String de10Below;
    @JsonProperty("de10_excess_de30_below")
    private String de10ExcessDe30Below;
    @JsonProperty("de30_excess_de90_below")
    private String de30ExcessDe90Below;
    @JsonProperty("de90_excess_de180_below")
    private String de90ExcessDe180Below;
    @JsonProperty("de180_excess_yy1_below")
    private String de180ExcessYy1Below;
    @JsonProperty("sm")
    private String sm;
    @JsonProperty("isu_lmt")
    private String isuLmt;
    @JsonProperty("remndr_lmt")
    private String remndrLmt;
    @JsonProperty("stlm_dt")
    private String stlmDt;
}
