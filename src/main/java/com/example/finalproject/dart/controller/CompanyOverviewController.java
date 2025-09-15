package com.example.finalproject.dart.controller;


import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListRequestDto;
import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.finalproject.dart.entity.CompanyOverview;
import com.example.finalproject.dart.service.DbService;
import com.example.finalproject.dart.dto.common.MyApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
// @CrossOrigin 제거 (글로벌 CORS 설정으로 대체)
public class CompanyOverviewController {

    private final DbService dbService;

    /**
     * 기업 정보 저장 (Create)
     */
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

    /**
     * 테스트용 엔드포인트
     */
    @GetMapping("/test")
    public List<CompanyOverview> test(@RequestParam String word){
        return dbService.test(word);
    }

    /**
     * 모든 기업 정보 가져오기
     */
    @GetMapping
    public CompanyOverviewListResponseDto getAllCompanies() {
        return dbService.getAllCompanyOverviews();
    }

    /**
     * 키워드 기반 상위 100개 기업 정보 가져오기
     */
    @GetMapping("/search")
    public CompanyOverviewListResponseDto get100Companies(@RequestParam String keyword) {
        return dbService.get100CorpCode(keyword);
    }
}
