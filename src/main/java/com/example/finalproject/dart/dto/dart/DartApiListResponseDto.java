package com.example.finalproject.dart.dto.dart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DartApiListResponseDto {
    private String status;
    private String message;

    @JsonProperty("page_no")
    private Integer pageNo;

    @JsonProperty("page_count")
    private Integer pageCount;

    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("total_page")
    private Integer totalPage;

    private List<DartApiDto> list;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DartApiDto {
        @JsonProperty("corp_code")
        private String corpCode;

        @JsonProperty("corp_name")
        private String corpName;

        @JsonProperty("stock_code")
        private String stockCode;

        @JsonProperty("corp_cls")
        private String corpCls;

        @JsonProperty("report_nm")
        private String reportNm;

        @JsonProperty("rcept_no")
        private String rceptNo;

        @JsonProperty("flr_nm")
        private String flrNm;

        @JsonProperty("rcept_dt")
        private String rceptDt;

        private String rm;
    }
}