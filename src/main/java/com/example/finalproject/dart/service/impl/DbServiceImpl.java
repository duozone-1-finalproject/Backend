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

    public void saveCompanies(List<CompanyOverview> companies) {
        companyOverviewRepository.saveAll(companies);
    }

}
