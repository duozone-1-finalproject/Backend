package com.example.finalproject.dart.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyOverview {

    @JsonProperty("corp_code")
    private String corpCode;

    @JsonProperty("corp_name")
    private String corpName;

    @JsonProperty("corp_eng_name")
    private String corpEngName;

    @JsonProperty("stock_code")
    private String stockCode;

}
