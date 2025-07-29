package com.example.test_02.dart.dto.CompanyOverview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CompanyOverviewListResponseDto {
    private List<CompanyOverviewResponseDto> companies;
    private String resultMeg;

    public static CompanyOverviewListResponseDto from(List<CompanyOverviewResponseDto> dtoList) {
        return CompanyOverviewListResponseDto.builder()
                .companies(dtoList)
                .build();
    }
}