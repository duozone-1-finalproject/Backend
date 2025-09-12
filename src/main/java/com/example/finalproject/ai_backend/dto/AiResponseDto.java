// src/main/java/com/example/ai_backend/dto/AiResponseDto.java
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
public class AiResponseDto {

    @JsonProperty("request_id")
    private String requestId;       // 요청 ID

    @JsonProperty("generated_html")
    private String generatedHtml;   // AI가 생성한 완성된 HTML

    @JsonProperty("summary")
    private String summary;         // AI가 생성한 요약 (1-5문장)

    @JsonProperty("processing_time")
    private Long processingTime;    // 처리 시간 (밀리초)

    @JsonProperty("status")
    private String status;          // 처리 상태 (SUCCESS, FAILED)
}
