package com.example.finalproject.ai_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResponseDto {
    private String requestId;
    private String status; // "SUCCESS", "ERROR"
    private ValidationDto validationResult;
    private String errorMessage;
}