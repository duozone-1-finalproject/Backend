package com.example.finalproject.dart.controller;


import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListRequestDto;
import com.example.finalproject.dart.entity.CompanyOverview;
import com.example.finalproject.dart.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyOverviewController {

    private final DbService dbService;

    @PostMapping("/save")
    public String saveCompanies(@RequestBody CompanyOverviewListRequestDto request) {
        List<CompanyOverview> companies = request.getCompanies().stream()
                .map(dto -> {
                    CompanyOverview company = new CompanyOverview();
                    company.setCorpCode(dto.getCorpCode());
                    company.setCorpName(dto.getCorpName());
                    return company;
                }).collect(Collectors.toList());

        dbService.saveCompanies(companies);
        return "저장 성공!";
    }
}
