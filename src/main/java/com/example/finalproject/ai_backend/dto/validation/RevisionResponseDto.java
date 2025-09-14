package com.example.finalproject.ai_backend.dto.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevisionResponseDto {
    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("status")
    private String status; // "SUCCESS", "ERROR"

    @JsonProperty("revised_content")
    private String revisedContent;

    @JsonProperty("error_message")
    private String errorMessage;
}