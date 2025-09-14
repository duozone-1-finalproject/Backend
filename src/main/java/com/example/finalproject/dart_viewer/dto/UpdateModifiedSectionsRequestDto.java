package com.example.finalproject.dart_viewer.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateModifiedSectionsRequestDto {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("corp_code")
    private String corpCode;
    private List<String> modifiedSections;
}
