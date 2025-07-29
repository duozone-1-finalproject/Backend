package com.example.test_02.dart.controller;


import com.example.dart.model.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.dart.model.dto.CompanyOverview.CompanyOverviewResponseDto;
import com.example.dart.model.dto.dart.DartApiListResponseDto;
import com.example.dart.model.dto.dart.DartDocumentListRequestDto;
import com.example.dart.model.service.DartApiService;
import com.example.dart.model.service.DartService;
import com.example.dart.model.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
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
