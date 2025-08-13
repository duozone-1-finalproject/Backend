package com.example.finalproject.dart.service.impl;

import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListRequestDto;
import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewResponseDto;
import com.example.finalproject.dart.entity.CompanyOverview;
import com.example.finalproject.dart.repository.CompanyOverviewRepository;
import com.example.finalproject.dart.service.DbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DbServiceImpl implements DbService {
    private final CompanyOverviewRepository companyOverviewRepository;


    // db에 기업코드 리스트 넣기
    public String storeCompanies(CompanyOverviewListRequestDto companyOverviewListRequestDto){

        List<CompanyOverview> companies = new ArrayList<>();

        try {
            companies = companyOverviewListRequestDto.getCompanies().stream()
                    .map(dto -> {
                        CompanyOverview company = new CompanyOverview();
                        company.setCorpCode(dto.getCorpCode());
                        company.setCorpName(dto.getCorpName());
                        company.setCorpEngName(dto.getCorpEngName());
                        company.setStockCode(dto.getStockCode());
                        return company;
                    }).toList();
            companyOverviewRepository.saveAll(companies);
            return "Sucess : Good";
        }catch (NullPointerException exception){
            log.error("error : package com.example.finalproject.dart.service.impl.DbServiceImpl.saveCorpCode() ");
            return "error : NullPointerException";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // 테스트
    public List<CompanyOverview> test(String word){
        try {
            return companyOverviewRepository.findByCorpNameContaining(word);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // db에서 기업들의 정보(기업이름,기업코드) 가져오기
    @Override
    public CompanyOverviewListResponseDto getAllCompanyOverviews(){
        // 컴퍼니 정보 전부 조회하기
        // http://localhost:8080/api/companies
        List<CompanyOverview> companyOverviewList = null;
        try {
            companyOverviewList = companyOverviewRepository.findAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
