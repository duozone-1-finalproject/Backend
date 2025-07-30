package com.example.finalproject.dart.service;

import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;

public interface DbService {
    // db에서 기업들의 정보(기업이름,기업코드) 가져오기
    CompanyOverviewListResponseDto getAllCompanyOverviews();
}
