package com.example.finalproject.ai_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto<T> {
    private long totalHits;
    private List<T> documents;
}