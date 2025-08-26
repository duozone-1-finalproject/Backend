package com.example.finalproject.apitest.dto.material.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartCocoBondIssuanceItem {

    @JsonProperty("rcept_no")
    private String rceptNo;
    @JsonProperty("corp_cls")
    private String corpCls;
    @JsonProperty("corp_code")
    private String corpCode;
    @JsonProperty("corp_name")
    private String corpName;
    @JsonProperty("bd_tm")
    private String bdTm;
    @JsonProperty("bd_knd")
    private String bdKnd;
    @JsonProperty("bd_fta")
    private String bdFta;
    @JsonProperty("ovis_fta")
    private String ovisFta;
    @JsonProperty("ovis_fta_crn")
    private String ovisFtaCrn;
    @JsonProperty("ovis_ster")
    private String ovisSter;
    @JsonProperty("ovis_isar")
    private String ovisIsar;
    @JsonProperty("ovis_mktnm")
    private String ovisMktnm;
    @JsonProperty("fdpp_fclt")
    private String fdppFclt;
    @JsonProperty("fdpp_bsninh")
    private String fdppBsninh;
    @JsonProperty("fdpp_op")
    private String fdppOp;
    @JsonProperty("fdpp_dtrp")
    private String fdppDtrp;
    @JsonProperty("fdpp_ocsa")
    private String fdppOcsa;
    @JsonProperty("fdpp_etc")
    private String fdppEtc;
    @JsonProperty("bd_itr_sf")
    private String bdItrSf;
    @JsonProperty("bd_intr_ex")
    private String bdIntrEx;
    @JsonProperty("bd_mtd")
    private String bdMtd;
    @JsonProperty("dbtrs_sc")
    private String dbtrsSc;
    @JsonProperty("sbd")
    private String sbd;
    @JsonProperty("pymd")
    private String pymd;
    @JsonProperty("rpcmcp")
    private String rpcmcp;
    @JsonProperty("grint")
    private String grint;
    @JsonProperty("bddd")
    private String bddd;
    @JsonProperty("od_a_at_b")
    private String odAAtB;
    @JsonProperty("od_a_at_t")
    private String odAAtT;
    @JsonProperty("adt_a_atn")
    private String adtAAtn;
    @JsonProperty("rs_sm_atn")
    private String rsSmAtn;
    @JsonProperty("ex_smr_r")
    private String exSmrR;
    @JsonProperty("ovis_ltdtl")
    private String ovisLtdtl;
    @JsonProperty("ftc_att_atn")
    private String ftcAttAtn;
}
