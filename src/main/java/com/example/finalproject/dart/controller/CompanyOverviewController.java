package com.example.finalproject.dart.controller;


import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListRequestDto;
import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.finalproject.dart.entity.CompanyOverview;
import com.example.finalproject.dart.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyOverviewController {
    private final DbService dbService; // ✅ 여기서 생성자 주입 받기

    // 기업 정보 넣기 C
    @PostMapping
    public String saveCompanies(@RequestBody CompanyOverviewListRequestDto dto){
        return dbService.storeCompanies(dto);
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
