package com.example.finalproject.apitest.entity.material;

// 전환사채권 발행결정
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_cb_issuance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartCbIssuance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rcept_no", length = 14, nullable = false)
    private String rceptNo; // 접수번호
    @Column(name = "corp_cls", length = 1)
    private String corpCls; // 법인구분
    @Column(name = "corp_code", length = 8)
    private String corpCode; // 고유번호
    @Column(name = "corp_name", length = 200)
    private String corpName; // 회사명
    @Column(name = "bd_tm", length = 100)
    private String bdTm; // 사채의 종류(회차)
    @Column(name = "bd_knd", length = 200)
    private String bdKnd; // 사채의 종류(종류)
    @Column(name = "bd_fta")
    private Long bdFta; // 사채의 권면(전자등록)총액 (원)
    @Column(name = "atcsc_rmismt")
    private Long atcscRmismt; // 정정전 잔여 발행한도
    @Column(name = "ovis_fta")
    private Long ovisFta; // 해외발행(권면(전자등록)총액)
    @Column(name = "ovis_fta_crn", length = 50)
    private String ovisFtaCrn; // 해외발행(권면(전자등록)총액(통화단위))
    @Column(name = "ovis_ster", length = 100)
    private String ovisSter; // 해외발행(기준환율등)
    @Column(name = "ovis_isar", length = 100)
    private String ovisIsar; // 해외발행(발행지역)
    @Column(name = "ovis_mktnm", length = 200)
    private String ovisMktnm; // 해외발행(해외상장시 시장의 명칭)
    @Column(name = "fdpp_fclt")
    private Long fdppFclt; // 자금조달의 목적(시설자금 (원))
    @Column(name = "fdpp_bsninh")
    private Long fdppBsninh; // 자금조달의 목적(영업양수자금 (원))
    @Column(name = "fdpp_op")
    private Long fdppOp; // 자금조달의 목적(운영자금 (원))
    @Column(name = "fdpp_dtrp")
    private Long fdppDtrp; // 자금조달의 목적(채무상환자금 (원))
    @Column(name = "fdpp_ocsa")
    private Long fdppOcsa; // 자금조달의 목적(타법인 증권 취득자금 (원))
    @Column(name = "fdpp_etc")
    private Long fdppEtc; // 자금조달의 목적(기타자금 (원))
    @Column(name = "bd_intr_ex", length = 50)
    private String bdIntrEx; // 사채의 이율(만기이자율 (%))
    @Column(name = "bd_intr_sf", length = 50)
    private String bdIntrSf; // 사채의 이율(표면이자율 (%))
    @Column(name = "bd_mtd")
    private LocalDate bdMtd; // 사채만기일
    @Column(name = "bdlis_mthn", length = 200)
    private String bdlisMthn; // 사채발행방법
    @Column(name = "cv_prc")
    private Long cvPrc; // 전환에 관한 사항(전환가액 (원/주))
    @Column(name = "cv_prc_dmth", columnDefinition = "TEXT")
    private String cvPrcDmth; // 전환에 관한 사항(전환가액 결정방법)
    @Column(name = "cv_rt", length = 50)
    private String cvRt; // 전환에 관한 사항(전환비율 (%))
    @Column(name = "cv_rqpd_bgd")
    private LocalDate cvRqpdBgd; // 전환에 관한 사항(전환청구기간(시작일))
    @Column(name = "cv_rqpd_edd")
    private LocalDate cvRqpdEdd; // 전환에 관한 사항(전환청구기간(종료일))
    @Column(name = "cv_isstk_knd", length = 200)
    private String cvIsstkKnd; // 전환에 따라 발행할 주식(종류)
    @Column(name = "cv_isstk_cnt")
    private Long cvIsstkCnt; // 전환에 따라 발행할 주식(주식 수)
    @Column(name = "cv_isstk_isstk_vs", length = 50)
    private String cvIsstkIsstkVs; // 발행할 주식(총주식수 대비 비율(%))
    @Column(name = "act_mktprcfl_cvprc_lwtrsprc")
    private Long actMktprcflCvprcLwtrsprc; // 시가하락에 따른 전환가액 조정(최저 조정가액 (원))
    @Column(name = "act_mktprcfl_cvprc_lwtrsprc_bs", columnDefinition = "TEXT")
    private String actMktprcflCvprcLwtrsprcBs; // 시가하락에 따른 전환가액 조정(최저 조정가액 근거))
    @Column(name = "rmislmt_lt70p")
    private Long rmislmtLt70p; // 발행당시 전환가액의 70% 미만으로 조정가능한 잔여 발행한도(원)
    @Column(name = "abmg", columnDefinition = "TEXT")
    private String abmg; // 합병 관련 사항
    @Column(name = "sbd")
    private LocalDate sbd; // 청약일
    @Column(name = "pymd")
    private LocalDate pymd; // 납입일
    @Column(name = "rpcmcp", length = 200)
    private String rpcmcp; // 대표주관회사
    @Column(name = "grint", length = 200)
    private String grint; // 보증기관
    @Column(name = "bddd")
    private LocalDate bddd; // 이사회의결일(결정일)
    @Column(name = "od_a_at_t")
    private Long odAAtT; // 사외이사 참석여부(참석 (명))
    @Column(name = "od_a_at_b")
    private Long odAAtB; // 사외이사 참석여부(불참 (명))
    @Column(name = "adt_a_atn", length = 100)
    private String adtAAtn; // 감사(감사위원) 참석여부
    @Column(name = "rs_sm_atn", length = 100)
    private String rsSmAtn; // 증권신고서 제출대상 여부
    @Column(name = "ex_smr_r", columnDefinition = "TEXT")
    private String exSmrR; // 제출을 면제받은 경우 그 사유
    @Column(name = "ovis_ltdtl", columnDefinition = "TEXT")
    private String ovisLtdtl; // 당해 사채의 해외발행과 연계된 대차거래 내역
    @Column(name = "ftc_stt_atn", length = 100)
    private String ftcSttAtn; // 공정거래위원회 신고대상 여부
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}