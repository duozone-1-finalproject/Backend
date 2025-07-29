package com.example.finalproject.dart.controller;


import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewResponseDto;
import com.example.finalproject.dart.dto.dart.DartApiListResponseDto;
import com.example.finalproject.dart.dto.dart.DartDocumentListRequestDto;
import com.example.finalproject.dart.service.DartApiService;
import com.example.finalproject.dart.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyOverviewController {
    private final DbService dbService; // ✅ 여기서 생성자 주입 받기

    // 기업 정보 전부 가져오기
    // http://localhost:8080/api/companies
    @GetMapping()
    public CompanyOverviewListResponseDto getAllCompanies(){
        return dbService.getAllCompanyOverviews();
    }

}
