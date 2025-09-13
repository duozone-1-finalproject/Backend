// src/main/java/com/example/finalproject/ai_backend/controller/DebugController.java
package com.example.finalproject.ai_backend.controller;

import com.example.finalproject.ai_backend.dto.ApiResponseDto2;
import com.example.finalproject.ai_backend.dto.VariableMappingRequestDto;
import com.example.finalproject.ai_backend.service.CompanyDataMappingService;
import com.example.finalproject.apitest.entity.overview.DartCompanyOverview;
import com.example.finalproject.apitest.repository.overview.DartCompanyOverviewRepository;
import com.example.finalproject.ai_backend.repository.IndustryTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 데이터 매핑 디버깅용 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/debug")
@RequiredArgsConstructor
public class DebugController {

    private final CompanyDataMappingService companyDataMappingService;
    private final DartCompanyOverviewRepository dartCompanyOverviewRepository;
    private final IndustryTableRepository industryTableRepository;

    /**
     * 회사 정보 확인
     * GET /api/v1/debug/company/{corpCode}
     */
    @GetMapping("/company/{corpCode}")
    public ResponseEntity<ApiResponseDto2<Map<String, Object>>> checkCompany(
            @PathVariable String corpCode) {

        log.info("회사 정보 디버깅: {}", corpCode);

        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 회사 존재 여부
            boolean exists = dartCompanyOverviewRepository.existsById(corpCode);
            result.put("exists", exists);

            if (exists) {
                // 2. 회사 정보 조회
                DartCompanyOverview company = dartCompanyOverviewRepository.findById(corpCode).get();
                Map<String, Object> companyInfo = new HashMap<>();
                companyInfo.put("corpCode", company.getCorpCode());
                companyInfo.put("corpName", company.getCorpName());
                companyInfo.put("indutyCode", company.getIndutyCode());
                companyInfo.put("stockName", company.getStockName());
                companyInfo.put("ceoNm", company.getCeoNm());

                result.put("companyInfo", companyInfo);

                // 3. 업종명 조회
                if (company.getIndutyCode() != null) {
                    String indutyName = industryTableRepository.findIndutyNameByCode(company.getIndutyCode())
                            .orElse("업종명을 찾을 수 없음");
                    result.put("indutyName", indutyName);
                }
            }

            ApiResponseDto2<Map<String, Object>> response =
                    ApiResponseDto2.success(result, "회사 정보 조회 완료");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("회사 정보 조회 실패", e);
            ApiResponseDto2<Map<String, Object>> response =
                    ApiResponseDto2.error("500", "조회 실패: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * AI 요청 데이터 생성 테스트
     * GET /api/v1/debug/ai-request/{corpCode}
     */
    @GetMapping("/ai-request/{corpCode}")
    public ResponseEntity<ApiResponseDto2<VariableMappingRequestDto>> createAiRequestTest(
            @PathVariable String corpCode) {

        log.info("AI 요청 데이터 생성 테스트: {}", corpCode);

        try {
            String testRequestId = "TEST_" + System.currentTimeMillis();
            VariableMappingRequestDto aiRequest = companyDataMappingService.createAiRequestData(corpCode, testRequestId);

            log.info("AI 요청 데이터 생성 성공:");
            log.info("  - requestId: {}", aiRequest.getRequestId());
            log.info("  - corpCode: {}", aiRequest.getCorpCode());
            log.info("  - corpName: {}", aiRequest.getCorpName());
            log.info("  - indutyCode: {}", aiRequest.getIndutyCode());
            log.info("  - indutyName: {}", aiRequest.getIndutyName());

            ApiResponseDto2<VariableMappingRequestDto> response =
                    ApiResponseDto2.success(aiRequest, "AI 요청 데이터 생성 완료");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("AI 요청 데이터 생성 실패", e);
            ApiResponseDto2<VariableMappingRequestDto> response =
                    ApiResponseDto2.error("500", "생성 실패: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 업종 테이블 확인
     * GET /api/v1/debug/industry
     */
    @GetMapping("/industry")
    public ResponseEntity<ApiResponseDto2<Map<String, Object>>> checkIndustryTable() {

        log.info("업종 테이블 확인");

        Map<String, Object> result = new HashMap<>();

        try {
            long totalCount = industryTableRepository.count();
            result.put("totalCount", totalCount);

            if (totalCount > 0) {
                // 처음 10개 레코드 조회
                var sampleData = industryTableRepository.findAll().stream()
                        .limit(10)
                        .map(industry -> {
                            Map<String, String> item = new HashMap<>();
                            item.put("indutyCode", industry.getIndutyCode());
                            item.put("indutyName", industry.getIndutyName());
                            return item;
                        })
                        .toList();

                result.put("sampleData", sampleData);
            }

            ApiResponseDto2<Map<String, Object>> response =
                    ApiResponseDto2.success(result, "업종 테이블 확인 완료");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("업종 테이블 확인 실패", e);
            ApiResponseDto2<Map<String, Object>> response =
                    ApiResponseDto2.error("500", "확인 실패: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    @GetMapping("/mapping-only/{corpCode}")
    public ResponseEntity<ApiResponseDto2<VariableMappingRequestDto>> testMappingOnly(
            @PathVariable String corpCode) {

        log.info("데이터 매핑만 테스트 (AI 전송 없음): {}", corpCode);

        try {
            // 1. 회사 존재 확인
            if (!companyDataMappingService.existsCompany(corpCode)) {
                ApiResponseDto2<VariableMappingRequestDto> response =
                        ApiResponseDto2.error("404", "존재하지 않는 회사입니다: " + corpCode);
                return ResponseEntity.status(404).body(response);
            }

            // 2. AI 요청 데이터 생성 (실제 AI에게 보내지는 않음)
            String testRequestId = "MAPPING_TEST_" + System.currentTimeMillis();
            VariableMappingRequestDto mappedData = companyDataMappingService.createAiRequestData(corpCode, testRequestId);

            log.info("=== 매핑된 데이터 확인 ===");
            log.info("requestId: {}", mappedData.getRequestId());
            log.info("corpCode: {}", mappedData.getCorpCode());
            log.info("corpName: {}", mappedData.getCorpName());
            log.info("indutyCode: {}", mappedData.getIndutyCode());
            log.info("indutyName: {}", mappedData.getIndutyName());

            ApiResponseDto2<VariableMappingRequestDto> response =
                    ApiResponseDto2.success(mappedData, "데이터 매핑 성공 (AI 전송하지 않음)");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("데이터 매핑 테스트 실패: {}", corpCode, e);
            ApiResponseDto2<VariableMappingRequestDto> response =
                    ApiResponseDto2.error("500", "매핑 실패: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 전체 데이터 플로우 테스트
     * GET /api/v1/debug/flow/{corpCode}
     */
    @GetMapping("/flow/{corpCode}")
    public ResponseEntity<ApiResponseDto2<String>> testDataFlow(@PathVariable String corpCode) {

        log.info("전체 데이터 플로우 테스트: {}", corpCode);

        try {
            companyDataMappingService.validateDataMapping(corpCode);

            ApiResponseDto2<String> response =
                    ApiResponseDto2.success("테스트 완료", "로그를 확인하세요");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("데이터 플로우 테스트 실패", e);
            ApiResponseDto2<String> response =
                    ApiResponseDto2.error("500", "테스트 실패: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}