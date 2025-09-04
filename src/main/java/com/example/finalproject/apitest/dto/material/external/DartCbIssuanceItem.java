package com.example.finalproject.apitest.dto.material.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartCbIssuanceItem {

    @JsonProperty("rcept_no")
    private String rceptNo; // 접수번호
    @JsonProperty("corp_cls")
    private String corpCls; // 법인구분
    @JsonProperty("corp_code")
    private String corpCode; // 고유번호
    @JsonProperty("corp_name")
    private String corpName; // 회사명
    @JsonProperty("bd_tm")
    private String bdTm; // 사채의 종류(회차)
    @JsonProperty("bd_knd")
    private String bdKnd; // 사채의 종류(종류)
    @JsonProperty("bd_fta")
    private String bdFta; // 사채의 권면(전자등록)총액 (원)
    @JsonProperty("atcsc_rmismt")
    private String atcscRmismt; // 정정전 잔여 발행한도
    @JsonProperty("ovis_fta")
    private String ovisFta; // 해외발행(권면(전자등록)총액)
    @JsonProperty("ovis_fta_crn")
    private String ovisFtaCrn; // 해외발행(권면(전자등록)총액(통화단위))
    @JsonProperty("ovis_ster")
    private String ovisSter; // 해외발행(기준환율등)
    @JsonProperty("ovis_isar")
    private String ovisIsar; // 해외발행(발행지역)
    @JsonProperty("ovis_mktnm")
    private String ovisMktnm; // 해외발행(해외상장시 시장의 명칭)
    @JsonProperty("fdpp_fclt")
    private String fdppFclt; // 자금조달의 목적(시설자금 (원))
    @JsonProperty("fdpp_bsninh")
    private String fdppBsninh; // 자금조달의 목적(영업양수자금 (원))
    @JsonProperty("fdpp_op")
    private String fdppOp; // 자금조달의 목적(운영자금 (원))
    @JsonProperty("fdpp_dtrp")
    private String fdppDtrp; // 자금조달의 목적(채무상환자금 (원))
    @JsonProperty("fdpp_ocsa")
    private String fdppOcsa; // 자금조달의 목적(타법인 증권 취득자금 (원))
    @JsonProperty("fdpp_etc")
    private String fdppEtc; // 자금조달의 목적(기타자금 (원))
    @JsonProperty("bd_intr_ex")
    private String bdIntrEx; // 사채의 이율(만기이자율 (%))
    @JsonProperty("bd_intr_sf")
    private String bdIntrSf; // 사채의 이율(표면이자율 (%))
    @JsonProperty("bd_mtd")
    private String bdMtd; // 사채만기일
    @JsonProperty("bdlis_mthn")
    private String bdlisMthn; // 사채발행방법
    @JsonProperty("cv_prc")
    private String cvPrc; // 전환에 관한 사항(전환가액 (원/주))
    @JsonProperty("cv_prc_dmth")
    private String cvPrcDmth; // 전환에 관한 사항(전환가액 결정방법)
    @JsonProperty("cv_rt")
    private String cvRt; // 전환에 관한 사항(전환비율 (%))
    @JsonProperty("cv_rqpd_bgd")
    private String cvRqpdBgd; // 전환에 관한 사항(전환청구기간(시작일))
    @JsonProperty("cv_rqpd_edd")
    private String cvRqpdEdd; // 전환에 관한 사항(전환청구기간(종료일))
    @JsonProperty("cv_isstk_knd")
    private String cvIsstkKnd; // 전환에 따라 발행할 주식(종류)
    @JsonProperty("cv_isstk_cnt")
    private String cvIsstkCnt; // 전환에 따라 발행할 주식(주식 수)
    @JsonProperty("cv_isstk_isstk_vs")
    private String cvIsstkIsstkVs; // 발행할 주식(총주식수 대비 비율(%))
    @JsonProperty("act_mktprcfl_cvprc_lwtrsprc")
    private String actMktprcflCvprcLwtrsprc; // 시가하락에 따른 전환가액 조정(최저 조정가액 (원))
    @JsonProperty("act_mktprcfl_cvprc_lwtrsprc_bs")
    private String actMktprcflCvprcLwtrsprcBs; // 시가하락에 따른 전환가액 조정(최저 조정가액 근거))
    @JsonProperty("rmislmt_lt70p")
    private String rmislmtLt70p; // 발행당시 전환가액의 70% 미만으로 조정가능한 잔여 발행한도(원)
    @JsonProperty("abmg")
    private String abmg; // 합병 관련 사항
    @JsonProperty("sbd")
    private String sbd; // 청약일
    @JsonProperty("pymd")
    private String pymd; // 납입일
    @JsonProperty("rpcmcp")
    private String rpcmcp; // 대표주관회사
    @JsonProperty("grint")
    private String grint; // 보증기관
    @JsonProperty("bddd")
    private String bddd; // 이사회의결일(결정일)
    @JsonProperty("od_a_at_t")
    private String odAAtT; // 사외이사 참석여부(참석 (명))
    @JsonProperty("od_a_at_b")
    private String odAAtB; // 사외이사 참석여부(불참 (명))
    @JsonProperty("adt_a_atn")
    private String adtAAtn; // 감사(감사위원) 참석여부
    @JsonProperty("rs_sm_atn")
    private String rsSmAtn; // 증권신고서 제출대상 여부
    @JsonProperty("ex_smr_r")
    private String exSmrR; // 제출을 면제받은 경우 그 사유
    @JsonProperty("ovis_ltdtl")
    private String ovisLtdtl; // 당해 사채의 해외발행과 연계된 대차거래 내역
    @JsonProperty("ftc_stt_atn")
    private String ftcSttAtn; // 공정거래위원회 신고대상 여부
}
