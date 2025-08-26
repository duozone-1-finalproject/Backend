package com.example.finalproject.apitest.controller;


import com.example.finalproject.apitest.dto.common.MyDartApiResponseDto;
import com.example.finalproject.apitest.dto.material.response.DartBwIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCbIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCocoBondIssuanceResponse;
import com.example.finalproject.apitest.dto.periodic.response.*;
import com.example.finalproject.apitest.exception.DartApiException;
import com.example.finalproject.apitest.service.TestService;
import com.example.finalproject.apitest.service.impl.TestServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/dart/test")
@RequiredArgsConstructor
// @CrossOrigin(origins = {"http://localhost:3000","http://localhost:5173"}, allowCredentials = "true")
public class TestController {

    private final TestService testService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDate today = LocalDate.now();
    String todayString = today.format(formatter);

    LocalDate oneYearAgo = today.minusYears(1);
    String oneYearAgoString = oneYearAgo.format(formatter);


    // 테스트 Get http://localhost:8080/api/dart/test/01571107/major-shareholder-statuses
    @GetMapping("/{corpCode}/major-shareholder-statuses")
    public MyDartApiResponseDto<List<DartMajorShareholderStatusResponse>> syncMajorShareholderStatus(@PathVariable String corpCode) {
        try{
            return MyDartApiResponseDto.ok(testService.DartMajorShareholderStatusCall(corpCode,"2024","11011"));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());

        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/major-shareholder-changes
    @GetMapping("/{corpCode}/major-shareholder-changes")
    public MyDartApiResponseDto<List<DartMajorShareholderChangeResponse>> syncMajorShareholderChange(@PathVariable String corpCode) {
        try{
            return MyDartApiResponseDto.ok(testService.DartMajorShareholderChangeCall(corpCode,"2024","11011"));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());

        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }

    // 테스트 Get http://localhost:8080/api/dart/test/01571107/executive-status
    @GetMapping("/{corpCode}/executive-status")
    public MyDartApiResponseDto<List<DartExecutiveStatusResponse>> syncDartExecutiveStatus(@PathVariable String corpCode) {
        try{
            return MyDartApiResponseDto.ok(testService.DartExecutiveStatusCall(corpCode,"2024","11011"));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());

        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }
    // 전환사채권 발행결정
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/cb-issuance
    @GetMapping("/{corpCode}/cb-issuance")
    public MyDartApiResponseDto<List<DartCbIssuanceResponse>> syncDartCbIssuance(@PathVariable String corpCode) {
        try{
            return MyDartApiResponseDto.ok(testService.DartCbIssuanceCall(corpCode,oneYearAgoString,todayString));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());

        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }

    // [추가] 신주인수권부사채권 발행결정
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/bw-issuance
    @GetMapping("/{corpCode}/bw-issuance")
    public MyDartApiResponseDto<List<DartBwIssuanceResponse>> syncDartBwIssuance(@PathVariable String corpCode) {
        try {
            // testService에 DartBwIssuanceCall 메소드가 구현되어 있어야 합니다.
            return MyDartApiResponseDto.ok(testService.DartBwIssuanceCall(corpCode, oneYearAgoString, todayString));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }

    // [추가] 주식의 총수 현황
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/total-stock-status
    @GetMapping("/{corpCode}/total-stock-status")
    public MyDartApiResponseDto<List<DartTotalStockStatusResponse>> syncDartTotalStockStatus(@PathVariable String corpCode) {
        try {
            // testService에 DartTotalStockStatusCall 메소드가 구현되어 있어야 합니다.
            return MyDartApiResponseDto.ok(testService.DartTotalStockStatusCall(corpCode, "2024", "11011"));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }

    // 자기주식 취득 및 처분 현황
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/treasury-stock-status
    @GetMapping("/{corpCode}/treasury-stock-status")
    public MyDartApiResponseDto<List<DartTreasuryStockStatusResponse>> syncDartTreasuryStockStatus(@PathVariable String corpCode) {
        try {
            return MyDartApiResponseDto.ok(testService.DartTreasuryStockStatusCall(corpCode, "2024", "11011"));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }

    // [추가] 단일회사 주요계정
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/single-company-key-account
    @GetMapping("/{corpCode}/single-company-key-account")
    public MyDartApiResponseDto<List<DartSingleCompanyKeyAccountResponse>> syncDartSingleCompanyKeyAccount(@PathVariable String corpCode) {
        try {
            // testService에 DartSingleCompanyKeyAccountCall 메소드가 구현되어 있어야 합니다.
            return MyDartApiResponseDto.ok(testService.DartSingleCompanyKeyAccountCall(corpCode, "2023", "11011"));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }

    // 단일회사 전체 재무제표
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/non-consolidated-fs?fsDiv=OFS
    @GetMapping("/{corpCode}/non-consolidated-fs")
    public MyDartApiResponseDto<List<DartNonConsolidatedFinancialStatementResponse>> syncNonConsolidatedFinancialStatement(
            @PathVariable String corpCode,
            @RequestParam(name = "fsDiv", defaultValue = "OFS") String fsDiv) {
        try {
            return MyDartApiResponseDto.ok(testService.DartNonConsolidatedFinancialStatementCall(corpCode, "2023", "11011", fsDiv));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }

    // 회사채 미상환 잔액
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/corporate-bond-balance
    @GetMapping("/{corpCode}/corporate-bond-balance")
    public MyDartApiResponseDto<List<DartCorporateBondBalanceResponse>> syncCorporateBondBalance(@PathVariable String corpCode) {
        try {
            return MyDartApiResponseDto.ok(testService.DartCorporateBondBalanceCall(corpCode, "2024", "11011"));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }

    // [추가] 기업어음증권 미상환 잔액
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/commercial-paper-balance
    @GetMapping("/{corpCode}/commercial-paper-balance")
    public MyDartApiResponseDto<List<DartCommercialPaperBalanceResponse>> syncCommercialPaperBalance(@PathVariable String corpCode) {
        try {
            return MyDartApiResponseDto.ok(testService.DartCommercialPaperBalanceCall(corpCode, "2024", "11011"));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }

    // 단기사채 미상환 잔액
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/short-term-bond-balance
    @GetMapping("/{corpCode}/short-term-bond-balance")
    public MyDartApiResponseDto<List<DartShortTermBondBalanceResponse>> syncShortTermBondBalance(@PathVariable String corpCode) {
        try {
            return MyDartApiResponseDto.ok(testService.DartShortTermBondBalanceCall(corpCode, "2024", "11011"));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }

    // [추가] 신종자본증권 미상환 잔액
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/hybrid-securities-balance
    @GetMapping("/{corpCode}/hybrid-securities-balance")
    public MyDartApiResponseDto<List<DartHybridSecuritiesBalanceResponse>> syncHybridSecuritiesBalance(@PathVariable String corpCode) {
        try {
            return MyDartApiResponseDto.ok(testService.DartHybridSecuritiesBalanceCall(corpCode, "2024", "11011"));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }

    // [추가] 상각형 조건부자본증권 발행결정
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/coco-bond-issuance
    @GetMapping("/{corpCode}/coco-bond-issuance")
    public MyDartApiResponseDto<List<DartCocoBondIssuanceResponse>> syncCocoBondIssuance(@PathVariable String corpCode) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate today = LocalDate.now();
            LocalDate oneYearAgo = today.minusYears(1);

            String todayString = today.format(formatter);
            String oneYearAgoString = oneYearAgo.format(formatter);

            return MyDartApiResponseDto.ok(testService.DartCocoBondIssuanceCall(corpCode, oneYearAgoString, todayString));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }

    // 공모자금의 사용내역
    // 테스트 Get http://localhost:8080/api/dart/test/01571107/public-offering-fund-usage
    @GetMapping("/{corpCode}/public-offering-fund-usage")
    public MyDartApiResponseDto<List<DartPublicOfferingFundUsageResponse>> syncPublicOfferingFundUsage(@PathVariable String corpCode) {
        try {
            return MyDartApiResponseDto.ok(testService.DartPublicOfferingFundUsageCall(corpCode, "2024", "11011"));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }
    }
}
