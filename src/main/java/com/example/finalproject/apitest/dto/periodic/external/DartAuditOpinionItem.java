package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartAuditOpinionItem {

    @JsonProperty("rcept_no")
    private String rceptNo;
    @JsonProperty("corp_cls")
    private String corpCls;
    @JsonProperty("corp_code")
    private String corpCode;
    @JsonProperty("corp_name")
    private String corpName;
    @JsonProperty("bsns_year")
    private String bsnsYear;
    @JsonProperty("adtor")
    private String adtor;
    @JsonProperty("adt_opinion")
    private String adtOpinion;
    @JsonProperty("adt_reprt_spcmnt_matter")
    private String adtReprtSpcmntMatter;
    @JsonProperty("emphs_matter")
    private String emphsMatter;
    @JsonProperty("core_adt_matter")
    private String coreAdtMatter;
    @JsonProperty("stlm_dt")
    private String stlmDt;
}
