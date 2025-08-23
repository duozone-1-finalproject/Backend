package com.example.finalproject.apitest.controller;


import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderStatusResponse;
import com.example.finalproject.apitest.service.TestService;
import com.example.finalproject.apitest.service.impl.TestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dart/test")
@RequiredArgsConstructor
// @CrossOrigin(origins = {"http://localhost:3000","http://localhost:5173"}, allowCredentials = "true")
public class TestController {

    private final TestService testService;

    @GetMapping()
    public List<DartMajorShareholderStatusResponse> test() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        LocalDate today = LocalDate.now();
        String todayString = today.format(formatter);

        LocalDate oneYearAgo = today.minusYears(1);
        String oneYearAgoString = oneYearAgo.format(formatter);

        try{

            return testService.testServ("01571107",oneYearAgoString,"11011");
        } catch (IOException e) {
            System.out.println("err");
            throw new RuntimeException(e);
        }

    }
}
