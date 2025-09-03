package com.example.finalproject.dart.controller;


import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListRequestDto;
import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
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

}
