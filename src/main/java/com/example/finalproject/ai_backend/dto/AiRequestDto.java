
// src/main/java/com/example/ai_backend/dto/AiRequestDto.java
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
public class AiRequestDto {

    @JsonProperty("template")
    private String template;        // HTML 템플릿

    @JsonProperty("company_data")
    private CompanyDataDto2 companyData;  // 회사 데이터

    @JsonProperty("request_id")
    private String requestId;       // 요청 ID (추적용)
}
