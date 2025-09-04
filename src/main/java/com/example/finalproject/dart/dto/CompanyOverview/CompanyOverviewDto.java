package com.example.finalproject.dart.dto.CompanyOverview;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyOverviewDto {
    @JsonProperty("corp_code")
    private String corpCode;

    @JsonProperty("corp_code")
    private String corpName;

    @JsonProperty("corp_eng_name")
    private String corpEngName;

    @JsonProperty("stock_code")
    private String stockCode;

    @JsonProperty("modify_date")
    private String modifyDate;
}
