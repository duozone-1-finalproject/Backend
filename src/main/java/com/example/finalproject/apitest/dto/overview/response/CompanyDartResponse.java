package com.example.finalproject.apitest.dto.overview.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDartResponse {

    private String status;        // 에러 및 정보 코드
    private String message;       // 에러 및 정보 메시지

    @JsonProperty("corp_name")
    private String corpName;      // 정식명칭

    @JsonProperty("corp_name_eng")
    private String corpNameEng;   // 영문명칭

    @JsonProperty("stock_name")
    private String stockName;     // 종목명(상장사) 또는 약식명칭(기타법인)

    @JsonProperty("stock_code")
    private String stockCode;     // 상장회사의 종목코드(6자리)

    @JsonProperty("ceo_nm")
    private String ceoNm;         // 대표자명

    @JsonProperty("corp_cls")
    private String corpCls;       // 법인구분 : Y(유가), K(코스닥), N(코넥스), E(기타)

    @JsonProperty("jurir_no")
    private String jurirNo;       // 법인등록번호

    @JsonProperty("bizr_no")
    private String bizrNo;        // 사업자등록번호

    @JsonProperty("adres")
    private String adres;         // 주소

    @JsonProperty("hm_url")
    private String hmUrl;         // 홈페이지

    @JsonProperty("ir_url")
    private String irUrl;         // IR홈페이지

    @JsonProperty("phn_no")
    private String phnNo;         // 전화번호

    @JsonProperty("fax_no")
    private String faxNo;         // 팩스번호

    @JsonProperty("induty_code")
    private String indutyCode;    // 업종코드

    @JsonProperty("est_dt")
    private String estDt;         // 설립일(YYYYMMDD)

    @JsonProperty("acc_mt")
    private String accMt;         // 결산월(MM)
}