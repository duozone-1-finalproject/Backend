package com.example.finalproject.reportViewer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateModifiedSectionsRequestDto {
    @JsonProperty("user_id")
    private Long userId;
    private List<String> modifiedSections;
}
