// src/main/java/com/example/finalproject/ai_backend/dto/VariableMappingRequestDto.java
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
public class VariableMappingRequestDto {

    @JsonProperty("request_id")
    private String requestId;       // 요청 ID (추적용)

    @JsonProperty("corp_code")
    private String corpCode;        // 회사 코드

    @JsonProperty("corp_name")
    private String corpName;        // 회사명

    @JsonProperty("induty_code")
    private String indutyCode;      // 업종 코드

    @JsonProperty("induty_name")
    private String indutyName;      // 업종명
}