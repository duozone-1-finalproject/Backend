package com.example.finalproject.ai_backend.dto.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CheckRequestDto {
    @JsonProperty("induty_name")
    private String indutyName;

    @JsonProperty("section")
    private String section;

    @JsonProperty("draft")
    private String draft;

}

