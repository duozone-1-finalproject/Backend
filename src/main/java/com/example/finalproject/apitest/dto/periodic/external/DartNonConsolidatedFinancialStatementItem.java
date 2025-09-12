package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartNonConsolidatedFinancialStatementItem {

    @JsonProperty("rcept_no")
    private String rceptNo;
    @JsonProperty("reprt_code")
    private String reprtCode;
    @JsonProperty("bsns_year")
    private String bsnsYear;
    @JsonProperty("corp_code")
    private String corpCode;
    @JsonProperty("sj_div")
    private String sjDiv;
    @JsonProperty("sj_nm")
    private String sjNm;
    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("account_nm")
    private String accountNm;
    @JsonProperty("account_detail")
    private String accountDetail;
    @JsonProperty("thstrm_nm")
    private String thstrmNm;
    @JsonProperty("thstrm_amount")
    private String thstrmAmount;
    @JsonProperty("thstrm_add_amount")
    private String thstrmAddAmount;
    @JsonProperty("frmtrm_nm")
    private String frmtrmNm;
    @JsonProperty("frmtrm_amount")
    private String frmtrmAmount;
    @JsonProperty("frmtrm_q_nm")
    private String frmtrmQNm;
    @JsonProperty("frmtrm_q_amount")
    private String frmtrmQAmount;
    @JsonProperty("frmtrm_add_amount")
    private String frmtrmAddAmount;
    @JsonProperty("bfefrmtrm_nm")
    private String bfefrmtrmNm;
    @JsonProperty("bfefrmtrm_amount")
    private String bfefrmtrmAmount;
    @JsonProperty("ord")
    private String ord;
    @JsonProperty("currency")
    private String currency;
}
