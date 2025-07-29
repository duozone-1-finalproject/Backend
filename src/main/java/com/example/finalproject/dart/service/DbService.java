package com.example.finalproject.dart.service;

import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewResponseDto;

public interface DbService {
    CompanyOverviewListResponseDto getAllCompanyOverviews();
}
