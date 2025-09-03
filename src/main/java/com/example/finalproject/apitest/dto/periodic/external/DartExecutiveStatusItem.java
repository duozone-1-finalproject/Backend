package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartExecutiveStatusItem {

    @JsonProperty("rcept_no")
    private String rceptNo; // 접수번호

    @JsonProperty("corp_cls")
    private String corpCls; // 법인구분

    @JsonProperty("corp_code")
    private String corpCode; // 고유번호

    @JsonProperty("corp_name")
    private String corpName; // 법인명

    @JsonProperty("nm")
    private String nm; // 성명

    @JsonProperty("sexdstn")
    private String sexdstn; // 성별

    @JsonProperty("birth_ym")
    private String birthYm; // 출생년월

    @JsonProperty("ofcps")
    private String ofcps; // 직위

    @JsonProperty("rgist_exctv_at")
    private String rgistExctvAt; // 등기임원여부

    @JsonProperty("fte_at")
    private String fteAt; // 상근여부

    @JsonProperty("chrg_job")
    private String chrgJob; // 담당업무

    @JsonProperty("main_career")
    private String mainCareer; // 주요경력

    @JsonProperty("mxmm_shrhldr_relate")
    private String mxmmShrhldrRelate; // 최대주주와의 관계

    @JsonProperty("hffc_pd")
    private String hffcPd; // 재직기간

    @JsonProperty("tenure_end_on")
    private String tenureEndOn; // 임기만료일

    @JsonProperty("stlm_dt")
    private String stlmDt; // 결산기준일
}