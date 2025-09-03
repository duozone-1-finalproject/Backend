// src/main/java/com/example/ai_backend/controller/TestController.java
package com.example.finalproject.ai_backend.controller;

import com.example.finalproject.ai_backend.dto.ApiResponseDto2;
import com.example.finalproject.ai_backend.dto.CompanyDataDto2;
import com.example.finalproject.ai_backend.service.CompanyDataService2;
import com.example.finalproject.ai_backend.service.TemplateService;
import com.example.finalproject.ai_backend.service.OpenSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@CrossOrigin(origins = "${frontend.url}")
public class TestController {

    private final CompanyDataService2 companyDataService;
    private final TemplateService templateService;
    private final OpenSearchService openSearchService;

    /**
     * 회사 데이터 조회 테스트
     * GET /api/v1/test/company/{corpCode}
     */
    @GetMapping("/company/{corpCode}")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<CompanyDataDto2>>> testGetCompanyData(
            @PathVariable String corpCode) {

        log.info("회사 데이터 조회 테스트: {}", corpCode);

        return companyDataService.getCompanyData(corpCode)
                .thenApply(companyData -> {
                    ApiResponseDto2<CompanyDataDto2> response = ApiResponseDto2.success(
                            companyData,
                            "회사 데이터 조회 테스트 성공"
                    );
                    return ResponseEntity.ok(response);
                })
                .exceptionally(throwable -> {
                    log.error("회사 데이터 조회 테스트 실패", throwable);
                    ApiResponseDto2<CompanyDataDto2> errorResponse = ApiResponseDto2.error(
                            "500",
                            "회사 데이터 조회 실패: " + throwable.getMessage()
                    );
                    return ResponseEntity.internalServerError().body(errorResponse);
                });
    }

    /**
     * 템플릿 변수 생성 테스트
     * POST /api/v1/test/template/variables
     */
    @PostMapping("/template/variables")
    public ResponseEntity<ApiResponseDto2<Map<String, String>>> testTemplateVariables(
            @RequestBody CompanyDataDto2 companyData) {

        log.info("템플릿 변수 생성 테스트: {}", companyData.getCorpName());

        try {
            Map<String, String> variables = templateService.createTemplateVariables(companyData);

            ApiResponseDto2<Map<String, String>> response = ApiResponseDto2.success(
                    variables,
                    "템플릿 변수 생성 테스트 성공"
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("템플릿 변수 생성 테스트 실패", e);
            ApiResponseDto2<Map<String, String>> errorResponse = ApiResponseDto2.error(
                    "500",
                    "템플릿 변수 생성 실패: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 완성된 템플릿 생성 테스트
     * POST /api/v1/test/template/complete
     */
    @PostMapping("/template/complete")
    public ResponseEntity<ApiResponseDto2<String>> testCompleteTemplate(
            @RequestBody CompanyDataDto2 companyData) {

        log.info("완성된 템플릿 생성 테스트: {}", companyData.getCorpName());

        try {
            // 기본 템플릿 가져오기
            String template = templateService.getSecuritiesRegistrationTemplate();

            // 변수 생성
            Map<String, String> variables = templateService.createTemplateVariables(companyData);

            // 템플릿에 변수 적용
            String completeTemplate = templateService.applyVariablesToTemplate(template, variables);

            ApiResponseDto2<String> response = ApiResponseDto2.success(
                    completeTemplate,
                    "완성된 템플릿 생성 테스트 성공"
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("완성된 템플릿 생성 테스트 실패", e);
            ApiResponseDto2<String> errorResponse = ApiResponseDto2.error(
                    "500",
                    "완성된 템플릿 생성 실패: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * OpenSearch 연결 테스트
     * GET /api/v1/test/opensearch/connection
     */
    @GetMapping("/opensearch/connection")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<String>>> testOpenSearchConnection() {
        log.info("OpenSearch 연결 테스트");

        return openSearchService.searchReportsByKeyword("test")
                .thenApply(searchResponse -> {
                    ApiResponseDto2<String> response = ApiResponseDto2.success(
                            "연결 성공 - 총 " + searchResponse.getHits().getTotalHits().value + "건 조회",
                            "OpenSearch 연결 테스트 성공"
                    );
                    return ResponseEntity.ok(response);
                })
                .exceptionally(throwable -> {
                    log.error("OpenSearch 연결 테스트 실패", throwable);
                    ApiResponseDto2<String> errorResponse = ApiResponseDto2.error(
                            "500",
                            "OpenSearch 연결 실패: " + throwable.getMessage()
                    );
                    return ResponseEntity.internalServerError().body(errorResponse);
                });
    }

    /**
     * 전체 프로세스 통합 테스트
     * GET /api/v1/test/integration/{corpCode}
     */
    @GetMapping("/integration/{corpCode}")
    public CompletableFuture<ResponseEntity<ApiResponseDto2<String>>> testIntegration(
            @PathVariable String corpCode) {

        log.info("통합 테스트 시작: {}", corpCode);

        return companyDataService.getCompanyData(corpCode)
                .thenApply(companyData -> {
                    // 1단계: 회사 데이터 조회 완료
                    log.info("1단계 완료: 회사 데이터 조회 - {}", companyData.getCorpName());

                    // 2단계: 템플릿 변수 생성
                    Map<String, String> variables = templateService.createTemplateVariables(companyData);
                    log.info("2단계 완료: 템플릿 변수 생성 - {} 개 변수", variables.size());

                    // 3단계: 완성된 템플릿 생성
                    String template = templateService.getSecuritiesRegistrationTemplate();
                    String completeTemplate = templateService.applyVariablesToTemplate(template, variables);
                    log.info("3단계 완료: 완성된 템플릿 생성 - {} 글자", completeTemplate.length());

                    ApiResponseDto2<String> response = ApiResponseDto2.success(
                            "통합 테스트 성공 - " + companyData.getCorpName(),
                            "모든 단계가 정상적으로 완료되었습니다."
                    );

                    return ResponseEntity.ok(response);
                })
                .exceptionally(throwable -> {
                    log.error("통합 테스트 실패: {}", corpCode, throwable);
                    ApiResponseDto2<String> errorResponse = ApiResponseDto2.error(
                            "500",
                            "통합 테스트 실패: " + throwable.getMessage()
                    );
                    return ResponseEntity.internalServerError().body(errorResponse);
                });
    }}