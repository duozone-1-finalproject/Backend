package com.example.finalproject.dart.controller;


import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListRequestDto;
import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.finalproject.dart.dto.IndutyTableResponseDto;
import com.example.finalproject.dart.entity.CompanyOverview;
import com.example.finalproject.dart.service.DbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import com.example.finalproject.dart.dto.common.MyApiResponseDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
@CrossOrigin(origins = "http://localhost:3000")
public class CompanyOverviewController {
    private final DbService dbService; // ✅ 여기서 생성자 주입 받기

    // 기업 정보 넣기 C
    @PostMapping
    public MyApiResponseDto<String> saveCompanies(@RequestBody CompanyOverviewListRequestDto dto) {
        try {
            String resultMessage = dbService.storeCompanies(dto);
            return MyApiResponseDto.ok(resultMessage);
        } catch (Exception e) {
            log.error("기업 정보 저장 중 에러 발생: {}", e.getMessage());
            return MyApiResponseDto.error(e.getMessage());
        }
    }

    // 테스트
    @GetMapping("/test")
    public List<CompanyOverview> test(@RequestParam String word){
        return dbService.test(word);
    }


    // 기업 정보 전부 가져오기
    // http://localhost:8080/api/companies
    @GetMapping()
    public CompanyOverviewListResponseDto getAllCompanies(){
        return dbService.getAllCompanyOverviews();
    }


    // 기업 정보 키워드로 100개 가져오기
    @GetMapping("/search")
    public CompanyOverviewListResponseDto get100Companies(@RequestParam String keyword){
        return dbService.get100CorpCode(keyword);
    }

    // 산업 코드로 산업 정보 조회
    @GetMapping("/industries/{industryCode}")
    public MyApiResponseDto<IndutyTableResponseDto> getIndustryByCode(@PathVariable String industryCode) {
        // IndustryResponseDto는 직접 만드셔야 하는, 산업 정보 응답을 위한 DTO입니다.
        try {
            IndutyTableResponseDto industry = dbService.getIndutyName(industryCode);
            return MyApiResponseDto.ok(industry);
        } catch (Exception e) {
            log.error("산업 정보 조회 중 에러 발생: {}", e.getMessage());
            return MyApiResponseDto.error("산업 정보를 조회하는 중 오류가 발생했습니다.");
        }
    }

}
