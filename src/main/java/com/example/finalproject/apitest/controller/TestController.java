package com.example.finalproject.apitest.controller;


import com.example.finalproject.apitest.dto.common.MyDartApiResponseDto;
import com.example.finalproject.apitest.dto.material.response.DartBwIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCbIssuanceResponse;
import com.example.finalproject.apitest.dto.periodic.response.DartExecutiveStatusResponse;
import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderChangeResponse;
import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderStatusResponse;
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
}
