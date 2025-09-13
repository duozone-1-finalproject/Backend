package com.example.finalproject.dart_viewer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetCompanyVersionsRequestDto {
    @JsonProperty("user_id")
    private Long userId;
    private String corpCode;
}
