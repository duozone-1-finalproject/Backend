package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartOutsideDirectorChangeStatusItem {

    @JsonProperty("rcept_no")
    private String rceptNo;
    @JsonProperty("corp_cls")
    private String corpCls;
    @JsonProperty("corp_code")
    private String corpCode;
    @JsonProperty("corp_name")
    private String corpName;
    @JsonProperty("drctr_co")
    private String drctrCo;
    @JsonProperty("otcmp_drctr_co")
    private String otcmpDrctrCo;
    @JsonProperty("apnt")
    private String apnt;
    @JsonProperty("rlsofc")
    private String rlsofc;
    @JsonProperty("mdstrm_resig")
    private String mdstrmResig;
    @JsonProperty("stlm_dt")
    private String stlmDt;
}
