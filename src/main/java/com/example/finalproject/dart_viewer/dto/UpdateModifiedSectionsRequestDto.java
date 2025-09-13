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
    private String corpCode;
    private String corpName;
    private List<String> modifiedSections;
}
