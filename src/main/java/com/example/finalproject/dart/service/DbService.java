package com.example.finalproject.dart.service;

import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.finalproject.dart.entity.CompanyOverview;



import java.util.List;

public interface DbService {
    // db에서 기업들의 정보(기업이름,기업코드) 넣기
    void saveCompanies(List<CompanyOverview> companies);


}
