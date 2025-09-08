package com.example.finalproject.ai_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FastAPI 서버에서 Kafka를 통해 받을 주식 공모 주석 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FastApiResponseDto {

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("status")
    private String status; // "SUCCESS", "FAILED", "PROCESSING"

    @JsonProperty("company_name")
    private String companyName;

    // 생성된 주석들
    @JsonProperty("S4_NOTE1_1")
    private String S4_NOTE1_1;

    @JsonProperty("S4_NOTE1_2")
    private String S4_NOTE1_2;

    @JsonProperty("S4_NOTE1_3")
    private String S4_NOTE1_3;

    @JsonProperty("S4_NOTE1_4")
    private String S4_NOTE1_4;

    @JsonProperty("S4_NOTE1_5")
    private String S4_NOTE1_5;

    // 처리 정보
    @JsonProperty("processing_time_ms")
    private Long processingTimeMs;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("error_message")
    private String errorMessage;

    public EquityAnnotationResponseDto toEquityResponse() {
        return EquityAnnotationResponseDto.builder()
                .S4_NOTE1_1(this.S4_NOTE1_1)
                .S4_NOTE1_2(this.S4_NOTE1_2)
                .S4_NOTE1_3(this.S4_NOTE1_3)
                .S4_NOTE1_4(this.S4_NOTE1_4)
                .S4_NOTE1_5(this.S4_NOTE1_5)
                .company_name(this.companyName)
                .processing_status("SUCCESS".equals(this.status) ? "COMPLETED" : "FAILED")
                .processing_time_ms(this.processingTimeMs)
                .timestamp(this.timestamp)
                .build();
    }

    public boolean isSuccess() {
        return "SUCCESS".equals(this.status);
    }

    public boolean isFailed() {
        return "FAILED".equals(this.status);
    }
}