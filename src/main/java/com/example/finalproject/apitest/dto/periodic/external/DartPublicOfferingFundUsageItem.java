package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartPublicOfferingFundUsageItem {

    @JsonProperty("rcept_no")
    private String rceptNo;
    @JsonProperty("corp_cls")
    private String corpCls;
    @JsonProperty("corp_code")
    private String corpCode;
    @JsonProperty("corp_name")
    private String corpName;
    @JsonProperty("se_nm")
    private String seNm;
    @JsonProperty("tm")
    private String tm;
    @JsonProperty("pay_de")
    private String payDe;
    @JsonProperty("pay_amount")
    private String payAmount;
    @JsonProperty("on_dclrt_cptal_use_plan")
    private String onDclrtCptalUsePlan;
    @JsonProperty("real_cptal_use_sttus")
    private String realCptalUseSttus;
    @JsonProperty("rs_cptal_use_plan_useprps")
    private String rsCptalUsePlanUseprps;
    @JsonProperty("rs_cptal_use_plan_picure_amount")
    private String rsCptalUsePlanPicureAmount;
    @JsonProperty("real_cptal_use_dtls_cn")
    private String realCptalUseDtlsCn;
    @JsonProperty("real_cptal_use_dtls_amount")
    private String realCptalUseDtlsAmount;
    @JsonProperty("dffrnc_occrrnc_resn")
    private String dffrncOccrrncResn;
    @JsonProperty("stlm_dt")
    private String stlmDt;
}
