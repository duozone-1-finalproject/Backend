package com.example.apitest.controller;

import com.example.apitest.dto.CompanyOverviewResponse;
import com.example.apitest.service.DartService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dart/reports")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:5173"}, allowCredentials = "true")
public class ReportController {

    private final DartService dartService;

    public ReportController(DartService dartService) {
        this.dartService = dartService;
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

        return dartService.getCompanyOverviewByCorpCode(useCorp);
    }
}
