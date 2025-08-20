package com.example.finalproject.reportViewer.dto;

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
    private String version;
    private String description; //null 가능
    private final Map<String, String> sectionsData = new HashMap<>();
    private String createdAt;
}
