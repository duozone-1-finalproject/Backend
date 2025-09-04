package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartSingleCompanyKeyAccountItem {

    @JsonProperty("rcept_no")
    private String rceptNo;
    @JsonProperty("bsns_year")
    private String bsnsYear;
    @JsonProperty("stock_code")
    private String stockCode;
    @JsonProperty("reprt_code")
    private String reprtCode;
    @JsonProperty("account_nm")
    private String accountNm;
    @JsonProperty("fs_div")
    private String fsDiv;
    @JsonProperty("fs_nm")
    private String fsNm;
    @JsonProperty("sj_div")
    private String sjDiv;
    @JsonProperty("sj_nm")
    private String sjNm;
    @JsonProperty("thstrm_nm")
    private String thstrmNm;
    @JsonProperty("thstrm_dt")
    private String thstrmDt;
    @JsonProperty("thstrm_amount")
    private String thstrmAmount;
    @JsonProperty("thstrm_add_amount")
    private String thstrmAddAmount;
    @JsonProperty("frmtrm_nm")
    private String frmtrmNm;
    @JsonProperty("frmtrm_dt")
    private String frmtrmDt;
    @JsonProperty("frmtrm_amount")
    private String frmtrmAmount;
    @JsonProperty("frmtrm_add_amount")
    private String frmtrmAddAmount;
    @JsonProperty("bfefrmtrm_nm")
    private String bfefrmtrmNm;
    @JsonProperty("bfefrmtrm_dt")
    private String bfefrmtrmDt;
    @JsonProperty("bfefrmtrm_amount")
    private String bfefrmtrmAmount;
    @JsonProperty("ord")
    private String ord;
    @JsonProperty("currency")
    private String currency;
}
