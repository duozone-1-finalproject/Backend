package com.example.finalproject.dart.service.impl;

import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewResponseDto;
import com.example.finalproject.dart.entity.CompanyOverview;
import com.example.finalproject.dart.repository.CompanyOverviewRepository;
import com.example.finalproject.dart.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DbServiceImpl implements DbService {
    private final CompanyOverviewRepository companyOverviewRepository;

    // db에서 기업들의 정보(기업이름,기업코드) 가져오기
    @Override
    public CompanyOverviewListResponseDto getAllCompanyOverviews(){
        // 컴퍼니 정보 전부 조회하기
        // http://localhost:8080/api/companies
        List<CompanyOverview> companyOverviewList = companyOverviewRepository.findAll();

        List<CompanyOverviewResponseDto> responseDtoList = companyOverviewList.stream()
                .map(company -> CompanyOverviewResponseDto.builder()
                        .corpCode(company.getCorpCode())
                        .corpName(company.getCorpName())
                        .build())
                .toList();

        return CompanyOverviewListResponseDto.builder()
                .companies(responseDtoList)
                .build();

    }
}
