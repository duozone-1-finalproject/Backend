package com.example.finalproject.dart.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyOverview {
// private static final String INDEX_NAME = "company_overview";
    @JsonProperty("corp_code")
    private String corpCode;

    @JsonProperty("corp_name")
    private String corpName;

    @JsonProperty("corp_eng_name")
    private String corpEngName;

    @JsonProperty("stock_code")
    private String stockCode;

}
