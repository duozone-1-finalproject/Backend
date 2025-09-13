package com.example.finalproject.dart_viewer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class CreateVersionRequestDto {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("corp_code")
    private String corpCode;
    private String corpName;
    private String version;
    private Long versionNumber;
    private String description; //null 가능
    private final Map<String, String> sectionsData = new HashMap<>();
    private String createdAt;

}