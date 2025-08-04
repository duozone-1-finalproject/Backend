package com.example.finalproject.dart.dto.CompanyOverview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CompanyOverviewListResponseDto {
    private String resultMeg;
    private List<CompanyOverviewResponseDto> companies;


    public static CompanyOverviewListResponseDto from(List<CompanyOverviewResponseDto> dtoList) {
        return CompanyOverviewListResponseDto.builder()
                .companies(dtoList)
                .build();
    }
}