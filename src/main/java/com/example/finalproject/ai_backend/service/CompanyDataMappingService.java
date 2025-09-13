// src/main/java/com/example/finalproject/ai_backend/service/CompanyDataMappingService.java
package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.VariableMappingRequestDto;
import com.example.finalproject.ai_backend.repository.IndustryTableRepository;
import com.example.finalproject.apitest.entity.overview.DartCompanyOverview;
import com.example.finalproject.apitest.repository.overview.DartCompanyOverviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyDataMappingService {

    private final DartCompanyOverviewRepository dartCompanyOverviewRepository;
    private final IndustryTableRepository industryTableRepository;

    /**
     * corp_code로 회사 정보를 조회하고 AI 요청용 DTO로 변환
     * AI에게 보낼 4개 변수: corpCode, corpName, indutyCode, indutyName
     */
    public VariableMappingRequestDto createAiRequestData(String corpCode, String requestId) {
        log.info("=== AI 요청 데이터 생성 시작 ===");
        log.info("입력 corpCode: {}", corpCode);

        // 1. DART 기업개황에서 회사 정보 조회
        DartCompanyOverview companyEntity = dartCompanyOverviewRepository.findById(corpCode)
                .orElseThrow(() -> {
                    log.error("기업개황 정보를 찾을 수 없습니다: {}", corpCode);
                    return new RuntimeException("기업개황 정보를 찾을 수 없습니다: " + corpCode);
                });

        log.info("회사 기본 정보 조회 완료:");
        log.info("  - corpCode: {}", companyEntity.getCorpCode());
        log.info("  - corpName: {}", companyEntity.getCorpName());
        log.info("  - indutyCode: {}", companyEntity.getIndutyCode());

        // 2. 업종코드로 업종명 조회
        String indutyName = getIndutyNameFromDb(companyEntity.getIndutyCode());
        log.info("업종명 조회 완료: {} -> {}", companyEntity.getIndutyCode(), indutyName);

        // 3. AI 요청 DTO 생성
        VariableMappingRequestDto aiRequest = VariableMappingRequestDto.builder()
                .requestId(requestId)
                .corpCode(companyEntity.getCorpCode())
                .corpName(companyEntity.getCorpName())
                .indutyCode(companyEntity.getIndutyCode())
                .indutyName(indutyName)
                .build();

        log.info("=== AI 요청 데이터 생성 완료 ===");
        log.info("AI에게 보낼 4개 변수:");
        log.info("  1. corpCode: {}", aiRequest.getCorpCode());
        log.info("  2. corpName: {}", aiRequest.getCorpName());
        log.info("  3. indutyCode: {}", aiRequest.getIndutyCode());
        log.info("  4. indutyName: {}", aiRequest.getIndutyName());

        return aiRequest;
    }

    /**
     * 업종코드를 IndustryTable DB에서 업종명으로 변환
     */
    private String getIndutyNameFromDb(String indutyCode) {
        if (indutyCode == null || indutyCode.trim().isEmpty()) {
            log.warn("업종코드가 null 또는 빈 값입니다.");
            return "업종정보없음";
        }

        log.info("업종명 DB 조회 시작: indutyCode={}", indutyCode);

        try {
            // IndustryTableRepository에서 업종명 조회
            String indutyName = industryTableRepository.findIndutyNameByCode(indutyCode)
                    .orElse(null);

            if (indutyName != null && !indutyName.trim().isEmpty()) {
                log.info("업종명 조회 성공: {} -> {}", indutyCode, indutyName);
                return indutyName;
            } else {
                log.warn("DB에서 업종명을 찾을 수 없습니다: {}", indutyCode);
                return "미분류업종(" + indutyCode + ")";
            }

        } catch (Exception e) {
            log.error("업종명 DB 조회 중 오류 발생: indutyCode={}", indutyCode, e);
            return "업종조회오류(" + indutyCode + ")";
        }
    }

    /**
     * 회사 존재 여부 확인
     */
    public boolean existsCompany(String corpCode) {
        boolean exists = dartCompanyOverviewRepository.existsById(corpCode);
        log.info("회사 존재 여부 확인: {} -> {}", corpCode, exists);
        return exists;
    }

    /**
     * 업종 테이블 데이터 확인 (디버깅용)
     */
    public void checkIndustryTableData() {
        try {
            long totalCount = industryTableRepository.count();
            log.info("IndustryTable 총 레코드 수: {}", totalCount);

            if (totalCount > 0) {
                // 몇 개 샘플 데이터 조회해서 로깅
                industryTableRepository.findAll().stream()
                        .limit(5)
                        .forEach(industry ->
                                log.info("샘플 업종 데이터: {} -> {}",
                                        industry.getIndutyCode(), industry.getIndutyName())
                        );
            } else {
                log.warn("IndustryTable에 데이터가 없습니다!");
            }
        } catch (Exception e) {
            log.error("IndustryTable 데이터 확인 중 오류", e);
        }
    }

    /**
     * 전체 데이터 조회 및 검증 (디버깅용)
     */
    public void validateDataMapping(String corpCode) {
        log.info("=== 데이터 매핑 전체 검증 ===");

        try {
            // 1. 회사 존재 확인
            boolean companyExists = existsCompany(corpCode);
            log.info("1. 회사 존재 여부: {}", companyExists);

            if (!companyExists) {
                log.error("회사가 존재하지 않습니다: {}", corpCode);
                return;
            }

            // 2. 회사 정보 조회
            DartCompanyOverview company = dartCompanyOverviewRepository.findById(corpCode).get();
            log.info("2. 회사 정보:");
            log.info("   - 회사명: {}", company.getCorpName());
            log.info("   - 업종코드: {}", company.getIndutyCode());

            // 3. 업종 테이블 확인
            checkIndustryTableData();

            // 4. 업종명 매핑 확인
            String indutyName = getIndutyNameFromDb(company.getIndutyCode());
            log.info("3. 최종 업종명: {}", indutyName);

            log.info("=== 검증 완료 ===");

        } catch (Exception e) {
            log.error("데이터 매핑 검증 중 오류", e);
        }
    }
}