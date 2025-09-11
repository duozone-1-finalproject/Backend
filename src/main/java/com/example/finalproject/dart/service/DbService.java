package com.example.finalproject.dart.service;

import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListRequestDto;
import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewRequestDto;
import com.example.finalproject.dart.dto.IndutyTableResponseDto;
import com.example.finalproject.dart.entity.CompanyOverview;

import java.util.List;
import java.util.Optional;

public interface DbService {


    // 엘라스틱
    // db에 기업코드 리스트 넣기
    String storeCompanies(CompanyOverviewListRequestDto dto);

    // 테스트용 함수
    List<CompanyOverview> test(String word);

    // db에서 기업들의 정보(기업이름,기업코드) 가져오기
    CompanyOverviewListResponseDto getAllCompanyOverviews();

    // db에서 키워드로 검색된 리스트 100개 출력
    CompanyOverviewListResponseDto get100CorpCode(String keyword);

    // db에서 산업코드를 통해 산업명 반환
    IndutyTableResponseDto getIndutyName(String indutyCode);
}
