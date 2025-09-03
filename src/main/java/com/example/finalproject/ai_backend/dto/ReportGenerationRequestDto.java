// src/main/java/com/example/ai_backend/dto/ReportGenerationRequestDto.java
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
public class ReportGenerationRequestDto {

    @JsonProperty("corp_code")
    private String corpCode;        // 회사 코드 (DB에서 조회용)

    @JsonProperty("report_type")
    private String reportType;      // 보고서 타입 (예: "증권신고서")

    @JsonProperty("template_id")
    private String templateId;      // 템플릿 ID
}