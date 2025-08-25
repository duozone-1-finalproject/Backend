package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartMajorShareholderStatusItem {
    @JsonProperty("rcept_no")
    private String rceptNo;
    @JsonProperty("corp_cls")
    private String corpCls;
    @JsonProperty("corp_code")
    private String corpCode;
    @JsonProperty("corp_name")
    private String corpName;
    private String nm;
    private String relate;
    @JsonProperty("stock_knd")
    private String stockKnd;
    @JsonProperty("bsis_posesn_stock_co")
    private String bsisPosesnStockCo;
    @JsonProperty("bsis_posesn_stock_qota_rt")
    private String bsisPosesnStockQotaRt;
    @JsonProperty("trmend_posesn_stock_co")
    private String trmendPosesnStockCo;
    @JsonProperty("trmend_posesn_stock_qota_rt")
    private String trmendPosesnStockQotaRt;
    private String rm;
    @JsonProperty("stlm_dt")
    private String stlmDt;
}
