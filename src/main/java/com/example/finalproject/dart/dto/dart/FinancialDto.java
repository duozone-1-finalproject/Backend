package com.example.finalproject.dart.dto.dart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FinancialDto {
    @JsonProperty("corp_code")
    private String corpCode;
}
