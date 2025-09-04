package com.example.finalproject.apitest.dto.periodic.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DartTreasuryStockStatusItem {

    @JsonProperty("rcept_no")
    private String rceptNo;
    @JsonProperty("corp_cls")
    private String corpCls;
    @JsonProperty("corp_code")
    private String corpCode;
    @JsonProperty("corp_name")
    private String corpName;
    @JsonProperty("acqs_mth1")
    private String acqsMth1;
    @JsonProperty("acqs_mth2")
    private String acqsMth2;
    @JsonProperty("acqs_mth3")
    private String acqsMth3;
    @JsonProperty("stock_knd")
    private String stockKnd;
    @JsonProperty("bsis_qy")
    private String bsisQy;
    @JsonProperty("change_qy_acqs")
    private String changeQyAcqs;
    @JsonProperty("change_qy_dsps")
    private String changeQyDsps;
    @JsonProperty("change_qy_incnr")
    private String changeQyIncnr;
    @JsonProperty("trmend_qy")
    private String trmendQy;
    @JsonProperty("rm")
    private String rm;
    @JsonProperty("stlm_dt")
    private String stlmDt;
}
