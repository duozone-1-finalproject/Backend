package com.example.finalproject.apitest.dto.equity.external;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EquityGeneralInfoItem {
    @JsonProperty("rcept_no") private String rceptNo;
    @JsonProperty("corp_cls") private String corpCls;
    @JsonProperty("corp_code") private String corpCode;
    @JsonProperty("corp_name") private String corpName;
    @JsonProperty("sbd") private String sbd;
    @JsonProperty("pymd") private String pymd;
    @JsonProperty("sband") private String sband;
    @JsonProperty("asand") private String asand;
    @JsonProperty("asstd") private String asstd;
    @JsonProperty("exstk") private String exstk;
    @JsonProperty("exprc") private String exprc;
    @JsonProperty("expd") private String expd;
    @JsonProperty("rpt_rcpn") private String rptRcpn;
}
