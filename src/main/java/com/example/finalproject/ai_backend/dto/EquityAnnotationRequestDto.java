package com.example.finalproject.ai_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * FastAPI로 전송할 주식 공모 주석 요청 DTO
 * 프론트엔드 매핑 구조(S4_11A_1 ~ S4_11C_5)와 동일
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquityAnnotationRequestDto {

    // 회사 기본 정보
    @NotBlank(message = "회사명은 필수입니다")
    @JsonProperty("company_name")
    private String company_name;

    @JsonProperty("ceo_name")
    private String ceo_name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("establishment_date")
    private String establishment_date;

    @JsonProperty("company_phone")
    private String company_phone;

    @JsonProperty("company_website")
    private String company_website;

    // 증권의 종류 (S4_11A)
    // 💡 @NotBlank 제거: DART API 응답에 해당 정보가 없을 수 있으므로 선택적으로 받도록 변경
    @JsonProperty("S4_11A_1")
    private String S4_11A_1; // security_type

    @JsonProperty("S4_11A_2")
    private String S4_11A_2; // security_count

    @JsonProperty("S4_11A_3")
    private String S4_11A_3; // face_value

    @JsonProperty("S4_11A_4")
    private String S4_11A_4; // offering_price_range

    @JsonProperty("S4_11A_5")
    private String S4_11A_5; // total_offering_amount

    @JsonProperty("S4_11A_6")
    private String S4_11A_6; // offering_method

    // 인수인 관련 (S4_11B)
    @JsonProperty("S4_11B_1")
    private String S4_11B_1; // underwriting_type

    @JsonProperty("S4_11B_2")
    private String S4_11B_2; // underwriter_name

    @JsonProperty("S4_11B_3")
    private String S4_11B_3; // underwriting_securities

    @JsonProperty("S4_11B_4")
    private String S4_11B_4; // underwriting_share_count

    @JsonProperty("S4_11B_5")
    private String S4_11B_5; // underwriting_amount

    @JsonProperty("S4_11B_6")
    private String S4_11B_6; // underwriting_price

    @JsonProperty("S4_11B_7")
    private String S4_11B_7; // underwriting_method

    // 청약 일정 (S4_11C)
    @JsonProperty("S4_11C_1")
    private String S4_11C_1; // subscription_basis

    @JsonProperty("S4_11C_2")
    private String S4_11C_2; // payment_date

    @JsonProperty("S4_11C_3")
    private String S4_11C_3; // subscription_period

    @JsonProperty("S4_11C_4")
    private String S4_11C_4; // allocation_date

    @JsonProperty("S4_11C_5")
    private String S4_11C_5; // expected_listing_date

    public String getCompanyName() {
        return company_name;
    }
}

