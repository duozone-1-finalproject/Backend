package com.example.finalproject.dart_viewer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyVersionsDto {
    private String corpCode;
    private String companyName;
    private Map<String, VersionResponseDto> versions;
}