package com.example.finalproject.apitest.dto.material.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartBwIssuanceItem {

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
    @JsonProperty("atcsc_rmismt")
    private String atcscRmismt;
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
    @JsonProperty("bd_intr_ex")
    private String bdIntrEx;
    @JsonProperty("bd_intr_sf")
    private String bdIntrSf;
    @JsonProperty("bd_mtd")
    private String bdMtd;
    @JsonProperty("bdlis_mthn")
    private String bdlisMthn;
    @JsonProperty("ex_rt")
    private String exRt;
    @JsonProperty("ex_prc")
    private String exPrc;
    @JsonProperty("ex_prc_dmth")
    private String exPrcDmth;
    @JsonProperty("bdwt_div_atn")
    private String bdwtDivAtn;
    @JsonProperty("nstk_pym_mth")
    private String nstkPymMth;
    @JsonProperty("nstk_isstk_knd")
    private String nstkIsstkKnd;
    @JsonProperty("nstk_isstk_cnt")
    private String nstkIsstkCnt;
    @JsonProperty("nstk_isstk_isstk_vs")
    private String nstkIsstkIsstkVs;
    @JsonProperty("expd_bgd")
    private String expdBgd;
    @JsonProperty("expd_edd")
    private String expdEdd;
    @JsonProperty("act_mktprcfl_cvprc_lwtrsprc")
    private String actMktprcflCvprcLwtrsprc;
    @JsonProperty("act_mktprcfl_cvprc_lwtrsprc_bs")
    private String actMktprcflCvprcLwtrsprcBs;
    @JsonProperty("rmislmt_lt70p")
    private String rmislmtLt70p;
    @JsonProperty("abmg")
    private String abmg;
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
    @JsonProperty("od_a_at_t")
    private String odAAtT;
    @JsonProperty("od_a_at_b")
    private String odAAtB;
    @JsonProperty("adt_a_atn")
    private String adtAAtn;
    @JsonProperty("rs_sm_atn")
    private String rsSmAtn;
    @JsonProperty("ex_smr_r")
    private String exSmrR;
    @JsonProperty("ovis_ltdtl")
    private String ovisLtdtl;
    @JsonProperty("ftc_stt_atn")
    private String ftcSttAtn;
}
