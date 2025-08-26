package com.example.finalproject.apitest.controller;


import com.example.finalproject.apitest.dto.common.MyDartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderStatusResponse;
import com.example.finalproject.apitest.exception.DartApiException;
import com.example.finalproject.apitest.service.TestService;
import com.example.finalproject.apitest.service.impl.TestServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping()
    public MyDartApiResponseDto<List<DartMajorShareholderStatusResponse>> test() {
        try{
            return MyDartApiResponseDto.ok(testService.testServ("01571107","2024","11011"));
        } catch (DartApiException e) {
            log.error("서비스 처리 중 에러 발생: {}", e.getMessage());
            return MyDartApiResponseDto.error(e.getMessage());

        } catch (Exception e) {
            log.error("알 수 없는 에러 발생", e);
            return MyDartApiResponseDto.error("알 수 없는 서버 오류가 발생했습니다.");
        }


    }
}
