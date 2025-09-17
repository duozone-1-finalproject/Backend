package com.example.finalproject.dart_viewer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class SaveEditingVersionRequestDto {
    @JsonProperty("user_id")
    private Long userId;
    private String description; //null 가능
    private final Map<String, String> sectionsData = new HashMap<>();
    private String createdAt;
}
