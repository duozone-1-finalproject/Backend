// File: dto/equity/external/EquityFundUsageItem.java
package com.example.finalproject.apitest.dto.equity.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EquityFundUsageItem {
    @JsonProperty("rcept_no") private String rceptNo;
    @JsonProperty("corp_cls") private String corpCls;
    @JsonProperty("corp_code") private String corpCode;
    @JsonProperty("corp_name") private String corpName;
    @JsonProperty("se") private String se;
    @JsonProperty("amt") private String amt;
}