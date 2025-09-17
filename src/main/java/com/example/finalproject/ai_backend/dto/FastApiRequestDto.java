package com.example.finalproject.ai_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FastAPI 서버로 Kafka를 통해 전송할 주식 공모 주석 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FastApiRequestDto {

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("request_type")
    private String requestType; // "EQUITY_ANNOTATION"

    // 회사 기본 정보
    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("ceo_name")
    private String ceoName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("establishment_date")
    private String establishmentDate;

    @JsonProperty("company_phone")
    private String companyPhone;

    @JsonProperty("company_website")
    private String companyWebsite;

    // 증권의 종류 (S4_11A)
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

    @JsonProperty("timestamp")
    private String timestamp;

    public static FastApiRequestDto fromEquityRequest(EquityAnnotationRequestDto request, String requestId) {
        return FastApiRequestDto.builder()
                .requestId(requestId)
                .requestType("EQUITY_ANNOTATION")
                .companyName(request.getCompany_name())
                .ceoName(request.getCeo_name())
                .address(request.getAddress())
                .establishmentDate(request.getEstablishment_date())
                .companyPhone(request.getCompany_phone())
                .companyWebsite(request.getCompany_website())
                .S4_11A_1(request.getS4_11A_1())
                .S4_11A_2(request.getS4_11A_2())
                .S4_11A_3(request.getS4_11A_3())
                .S4_11A_4(request.getS4_11A_4())
                .S4_11A_5(request.getS4_11A_5())
                .S4_11A_6(request.getS4_11A_6())
                .S4_11B_1(request.getS4_11B_1())
                .S4_11B_2(request.getS4_11B_2())
                .S4_11B_3(request.getS4_11B_3())
                .S4_11B_4(request.getS4_11B_4())
                .S4_11B_5(request.getS4_11B_5())
                .S4_11B_6(request.getS4_11B_6())
                .S4_11B_7(request.getS4_11B_7())
                .S4_11C_1(request.getS4_11C_1())
                .S4_11C_2(request.getS4_11C_2())
                .S4_11C_3(request.getS4_11C_3())
                .S4_11C_4(request.getS4_11C_4())
                .S4_11C_5(request.getS4_11C_5())
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();
    }
}