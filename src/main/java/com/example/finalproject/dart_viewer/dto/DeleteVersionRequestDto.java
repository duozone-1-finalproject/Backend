package com.example.finalproject.dart_viewer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteVersionRequestDto {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("corp_code")
    private String corpCode;
    private String version;
}