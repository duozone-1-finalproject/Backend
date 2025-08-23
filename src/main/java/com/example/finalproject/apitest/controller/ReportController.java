package com.example.finalproject.apitest.controller;

import com.example.finalproject.apitest.dto.overview.response.CompanyOverviewResponse;
import com.example.finalproject.apitest.service.DartService2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dart/reports")
// @CrossOrigin(origins = {"http://localhost:3000","http://localhost:5173"}, allowCredentials = "true")
public class ReportController {

    private final DartService2 dartService2;

    public ReportController(DartService2 dartService2) {
        this.dartService2 = dartService2;
    }

    // rceptNo → corpCode 임시 매핑 (없으면 기본값 사용)
    private static final Map<String, String> RNO_TO_CORP = Map.of(
            "20230323001052", "01571107" // 오픈엣지테크놀로지
    );

    @GetMapping("/{rceptNo}/company-overview")
    public CompanyOverviewResponse getCompanyOverview(
            @PathVariable String rceptNo,
            @RequestParam(value = "corpCode", required = false) String corpCode
    ) {
        String useCorp =
                (corpCode != null && !corpCode.isBlank())
                        ? corpCode
                        : RNO_TO_CORP.getOrDefault(rceptNo, "01571107");

        return dartService2.getCompanyOverviewByCorpCode(useCorp);
    }
}
