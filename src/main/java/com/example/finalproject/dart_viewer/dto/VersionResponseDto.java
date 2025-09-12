package com.example.finalproject.dart_viewer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionResponseDto {
    private String section1;
    private String section2;
    private String section3;
    private String section4;
    private String section5;
    private String section6;
    private String description;
    private String createdAt;
    private String modifiedSections;

}
