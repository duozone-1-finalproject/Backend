package com.example.finalproject.dart.dto.CompanyOverview;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CompanyOverviewResponseDto {
    private String corpCode;
    private String corpName;


    public static CompanyOverviewResponseDto fromRequest(CompanyOverviewDto dto) {
        return CompanyOverviewResponseDto.builder()
                .corpCode(dto.getCorpCode())
                .corpName(dto.getCorpName())
                .build();
    }

}
