package com.example.test_02.dart.service.impl;

import com.example.dart.model.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.dart.model.dto.CompanyOverview.CompanyOverviewResponseDto;
import com.example.dart.model.entity.CompanyOverview;
import com.example.dart.model.repository.CompanyOverviewRepository;
import com.example.dart.model.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DbServiceImpl implements DbService {
    private final CompanyOverviewRepository companyOverviewRepository;

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
