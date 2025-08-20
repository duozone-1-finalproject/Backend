package com.example.finalproject.apitest.dto.periodic.response;

// 전환사채권 발행결정
import com.example.finalproject.apitest.entity.periodic.DartCbIssuance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartCbIssuanceResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String bdTm; // 사채의 종류(회차)
    private String bdKnd; // 사채의 종류(종류)
    private Long bdFta; // 사채의 권면(전자등록)총액 (원)
    private Long atcscRmismt; // 정정전 잔여 발행한도
    private Long ovisFta; // 해외발행(권면(전자등록)총액)
    private String ovisFtaCrn; // 해외발행(권면(전자등록)총액(통화단위))
    private String ovisSter; // 해외발행(기준환율등)
    private String ovisIsar; // 해외발행(발행지역)
    private String ovisMktnm; // 해외발행(해외상장시 시장의 명칭)
    private Long fdppFclt; // 자금조달의 목적(시설자금 (원))
    private Long fdppBsninh; // 자금조달의 목적(영업양수자금 (원))
    private Long fdppOp; // 자금조달의 목적(운영자금 (원))
    private Long fdppDtrp; // 자금조달의 목적(채무상환자금 (원))
    private Long fdppOcsa; // 자금조달의 목적(타법인 증권 취득자금 (원))
    private Long fdppEtc; // 자금조달의 목적(기타자금 (원))
    private String bdIntrEx; // 사채의 이율(만기이자율 (%))
    private String bdIntrSf; // 사채의 이율(표면이자율 (%))
    private LocalDate bdMtd; // 사채만기일
    private String bdlisMthn; // 사채발행방법
    private Long cvPrc; // 전환에 관한 사항(전환가액 (원/주))
    private String cvPrcDmth; // 전환에 관한 사항(전환가액 결정방법)
    private String cvRt; // 전환에 관한 사항(전환비율 (%))
    private LocalDate cvRqpdBgd; // 전환에 관한 사항(전환청구기간(시작일))
    private LocalDate cvRqpdEdd; // 전환에 관한 사항(전환청구기간(종료일))
    private String cvIsstkKnd; // 전환에 따라 발행할 주식(종류)
    private Long cvIsstkCnt; // 전환에 따라 발행할 주식(주식 수)
    private String cvIsstkIsstkVs; // 발행할 주식(총주식수 대비 비율(%))
    private Long actMktprcflCvprcLwtrsprc; // 시가하락에 따른 전환가액 조정(최저 조정가액 (원))
    private String actMktprcflCvprcLwtrsprcBs; // 시가하락에 따른 전환가액 조정(최저 조정가액 근거))
    private Long rmislmtLt70p; // 발행당시 전환가액의 70% 미만으로 조정가능한 잔여 발행한도(원)
    private String abmg; // 합병 관련 사항
    private LocalDate sbd; // 청약일
    private LocalDate pymd; // 납입일
    private String rpcmcp; // 대표주관회사
    private String grint; // 보증기관
    private LocalDate bddd; // 이사회의결일(결정일)
    private Long odAAtT; // 사외이사 참석여부(참석 (명))
    private Long odAAtB; // 사외이사 참석여부(불참 (명))
    private String adtAAtn; // 감사(감사위원) 참석여부
    private String rsSmAtn; // 증권신고서 제출대상 여부
    private String exSmrR; // 제출을 면제받은 경우 그 사유
    private String ovisLtdtl; // 당해 사채의 해외발행과 연계된 대차거래 내역
    private String ftcSttAtn; // 공정거래위원회 신고대상 여부

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartCbIssuanceResponse from(DartCbIssuance entity) {
        return DartCbIssuanceResponse.builder()
                .rceptNo(entity.getRceptNo()).corpCls(entity.getCorpCls()).corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName()).bdTm(entity.getBdTm()).bdKnd(entity.getBdKnd())
                .bdFta(entity.getBdFta()).atcscRmismt(entity.getAtcscRmismt()).ovisFta(entity.getOvisFta())
                .ovisFtaCrn(entity.getOvisFtaCrn()).ovisSter(entity.getOvisSter()).ovisIsar(entity.getOvisIsar())
                .ovisMktnm(entity.getOvisMktnm()).fdppFclt(entity.getFdppFclt()).fdppBsninh(entity.getFdppBsninh())
                .fdppOp(entity.getFdppOp()).fdppDtrp(entity.getFdppDtrp()).fdppOcsa(entity.getFdppOcsa())
                .fdppEtc(entity.getFdppEtc()).bdIntrEx(entity.getBdIntrEx()).bdIntrSf(entity.getBdIntrSf())
                .bdMtd(entity.getBdMtd()).bdlisMthn(entity.getBdlisMthn()).cvPrc(entity.getCvPrc())
                .cvPrcDmth(entity.getCvPrcDmth()).cvRt(entity.getCvRt()).cvRqpdBgd(entity.getCvRqpdBgd())
                .cvRqpdEdd(entity.getCvRqpdEdd()).cvIsstkKnd(entity.getCvIsstkKnd())
                .cvIsstkCnt(entity.getCvIsstkCnt()).cvIsstkIsstkVs(entity.getCvIsstkIsstkVs())
                .actMktprcflCvprcLwtrsprc(entity.getActMktprcflCvprcLwtrsprc())
                .actMktprcflCvprcLwtrsprcBs(entity.getActMktprcflCvprcLwtrsprcBs())
                .rmislmtLt70p(entity.getRmislmtLt70p()).abmg(entity.getAbmg()).sbd(entity.getSbd())
                .pymd(entity.getPymd()).rpcmcp(entity.getRpcmcp()).grint(entity.getGrint())
                .bddd(entity.getBddd()).odAAtT(entity.getOdAAtT()).odAAtB(entity.getOdAAtB())
                .adtAAtn(entity.getAdtAAtn()).rsSmAtn(entity.getRsSmAtn()).exSmrR(entity.getExSmrR())
                .ovisLtdtl(entity.getOvisLtdtl()).ftcSttAtn(entity.getFtcSttAtn())
                .build();
    }
}