package com.example.finalproject.apitest.dto.periodic.response;
//임원 현황

import com.example.finalproject.apitest.entity.periodic.DartExecutiveStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DartExecutiveStatusResponse {

    private String rceptNo; // 접수번호
    private String corpCls; // 법인구분
    private String corpCode; // 고유번호
    private String corpName; // 법인명
    private String nm; // 성명
    private String sexdstn; // 성별
    private String birthYm; // 출생 년월
    private String ofcps; // 직위
    private String rgistExctvAt; // 등기 임원 여부
    private String fteAt; // 상근 여부
    private String chrgJob; // 담당 업무
    private String mainCareer; // 주요 경력
    private String mxmmShrhldrRelate; // 최대 주주 관계
    private String hffcPd; // 재직 기간
    private LocalDate tenureEndOn; // 임기 만료 일
    private LocalDate stlmDt; // 결산기준일

    /**
     * Entity 객체를 Response DTO 객체로 변환하는 정적 팩토리 메소드
     */
    public static DartExecutiveStatusResponse from(DartExecutiveStatus entity) {
        return DartExecutiveStatusResponse.builder()
                .rceptNo(entity.getRceptNo())
                .corpCls(entity.getCorpCls())
                .corpCode(entity.getCorpCode())
                .corpName(entity.getCorpName())
                .nm(entity.getNm())
                .sexdstn(entity.getSexdstn())
                .birthYm(entity.getBirthYm())
                .ofcps(entity.getOfcps())
                .rgistExctvAt(entity.getRgistExctvAt())
                .fteAt(entity.getFteAt())
                .chrgJob(entity.getChrgJob())
                .mainCareer(entity.getMainCareer())
                .mxmmShrhldrRelate(entity.getMxmmShrhldrRelate())
                .hffcPd(entity.getHffcPd())
                .tenureEndOn(entity.getTenureEndOn())
                .stlmDt(entity.getStlmDt())
                .build();
    }
}
