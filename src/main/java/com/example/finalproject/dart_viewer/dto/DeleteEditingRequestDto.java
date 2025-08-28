package com.example.finalproject.dart_viewer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteEditingRequestDto {
    @JsonProperty("user_id")
    private Long userId;
}
