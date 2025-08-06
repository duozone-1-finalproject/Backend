package com.example.finalproject.dart.dto.CompanyOverview;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CompanyOverviewListRequestDto {
    private List<CompanyOverviewRequestDto> companies;
}
