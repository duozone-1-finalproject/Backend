package com.example.test_02.dart.service;

import com.example.dart.model.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.dart.model.dto.CompanyOverview.CompanyOverviewResponseDto;

import java.util.List;

public interface DbService {
    CompanyOverviewListResponseDto getAllCompanyOverviews();
}
