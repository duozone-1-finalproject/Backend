package com.example.finalproject.ai_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRequestDto {
    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("induty_name")
    private String indutyName;

    @JsonProperty("section")
    private String section;

    @JsonProperty("draft")
    private String draft;
}
