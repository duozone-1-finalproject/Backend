package com.example.finalproject.reportViewer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FinalizeVersionRequestDto {
    @JsonProperty("user_id")
    private Long userId;
    private String description; //null 가능
    private String createdAt;
}
