// src/main/java/com/example/finalproject/ai_backend/dto/FrontendVariableResponseDto.java
package com.example.finalproject.ai_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrontendVariableResponseDto {

    @JsonProperty("S3_1A_1")
    private String s3_1a_1;         // 사업위험

    @JsonProperty("S3_1B_1")
    private String s3_1b_1;         // 회사위험

    @JsonProperty("S3_1C_1")
    private String s3_1c_1;         // 기타 투자위험

    @JsonProperty("s1_1d_1")
    private String s1_1d_1;

    public static FrontendVariableResponseDto from(VariableMappingResponseDto aiResponse) {
        return FrontendVariableResponseDto.builder()
                .s3_1a_1(aiResponse.getRiskIndustry())
                .s3_1b_1(aiResponse.getRiskCompany())
                .s3_1c_1(aiResponse.getRiskEtc())
                .s1_1d_1(aiResponse.getS1_1d_1())
                .build();
    }
}