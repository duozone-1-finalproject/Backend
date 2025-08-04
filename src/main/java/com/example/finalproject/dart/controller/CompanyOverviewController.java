package com.example.finalproject.dart.controller;


import com.example.finalproject.dart.dto.ApiResponse;
import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.finalproject.dart.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyOverviewController {
    private final DbService dbService; // ✅ 여기서 생성자 주입 받기

    // 기업 정보 전부 가져오기
    // http://localhost:8080/api/companies
    @GetMapping()
    public ResponseEntity<ApiResponse<CompanyOverviewListResponseDto>> getAllCompanies(){
        CompanyOverviewListResponseDto result = dbService.getAllCompanyOverviews();
        return ResponseEntity.ok(ApiResponse.success(result));
        //return dbService.getAllCompanyOverviews();
    }

}
