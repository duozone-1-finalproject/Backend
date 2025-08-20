package com.example.finalproject.apitest.dto.material.response;

// 상각형 조건부자본증권 발행결정
import com.example.finalproject.apitest.entity.material.DartCocoBondIssuance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartCocoBondIssuanceResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 회사명
    private String bdTm; // 사채의 종류(회차)
    private String bdKnd; // 사채의 종류(종류)
    private Long bdFta; // 사채의 권면(전자등록)총액 (원)
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
    private String bdItrSf; // 사채의 이율(표면이자율 (%))
    private String bdIntrEx; // 사채의 이율(만기이자율 (%))
    private LocalDate bdMtd; // 사채만기일
    private String dbtrsSc; // 채무재조정에 관한 사항(채무재조정의 범위)
    private LocalDate sbd; // 청약일
    private LocalDate pymd; // 납입일
    private String rpcmcp; // 대표주관회사
    private String grint; // 보증기관
    private LocalDate bddd; // 이사회의결일(결정일)
    private Long odAAtB; // 사외이사 참석여부(참석 (명))
    private Long odAAtT; // 사외이사 참석여부(불참 (명))
    private String adtAAtn; // 감사(감사위원) 참석여부
    private String rsSmAtn; // 증권신고서 제출대상 여부
    private String exSmrR; // 제출을 면제받은 경우 그 사유
    private String ovisLtdtl; // 당해 사채의 해외발행과 연계된 대차거래 내역
    private String ftcAttAtn; // 공정거래위원회 신고대상 여부

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartCocoBondIssuanceResponse from(DartCocoBondIssuance entity) {
        return DartCocoBondIssuanceResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .bdTm(entity.getBdTm())
                .bdKnd(entity.getBdKnd())
                .bdFta(entity.getBdFta())
                .ovisFta(entity.getOvisFta())
                .ovisFtaCrn(entity.getOvisFtaCrn())
                .ovisSter(entity.getOvisSter())
                .ovisIsar(entity.getOvisIsar())
                .ovisMktnm(entity.getOvisMktnm())
                .fdppFclt(entity.getFdppFclt())
                .fdppBsninh(entity.getFdppBsninh())
                .fdppOp(entity.getFdppOp())
                .fdppDtrp(entity.getFdppDtrp())
                .fdppOcsa(entity.getFdppOcsa())
                .fdppEtc(entity.getFdppEtc())
                .bdItrSf(entity.getBdItrSf())
                .bdIntrEx(entity.getBdIntrEx())
                .bdMtd(entity.getBdMtd())
                .dbtrsSc(entity.getDbtrsSc())
                .sbd(entity.getSbd())
                .pymd(entity.getPymd())
                .rpcmcp(entity.getRpcmcp())
                .grint(entity.getGrint())
                .bddd(entity.getBddd())
                .odAAtB(entity.getOdAAtB())
                .odAAtT(entity.getOdAAtT())
                .adtAAtn(entity.getAdtAAtn())
                .rsSmAtn(entity.getRsSmAtn())
                .exSmrR(entity.getExSmrR())
                .ovisLtdtl(entity.getOvisLtdtl())
                .ftcAttAtn(entity.getFtcAttAtn())
                .build();
    }
}