package com.example.finalproject.apitest.controller;

import com.example.finalproject.apitest.service.DartFetchUseCase;
import com.example.finalproject.apitest.service.DartQueryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/dart")
@RequiredArgsConstructor
public class DartApiController {

    private final DartFetchUseCase fetchUseCase;
    private final DartQueryUseCase queryUseCase;

    /** DART API 데이터 수집 */
    @PostMapping("/fetch")
    public ResponseEntity<?> fetchSecuritiesData(
            @RequestParam String corpCode,
            @RequestParam String beginDe,
            @RequestParam String endDe) {
        try {
            log.info("수집 요청 corpCode={}, begin={}, end={}", corpCode, beginDe, endDe);
            fetchUseCase.fetchAndSaveSecuritiesData(corpCode, beginDe, endDe);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "데이터 수집 및 저장 완료",
                    "corpCode", corpCode,
                    "period", beginDe + " ~ " + endDe
            ));
        } catch (Exception e) {
            log.error("수집 실패", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "수집 실패: " + e.getMessage()
            ));
        }
    }

    /** 회사별 조회 */
    @GetMapping("/company/{corpCode}")
    public ResponseEntity<?> getCompanyData(@PathVariable String corpCode) {
        try {
            Map<String, Object> data = (Map<String, Object>) queryUseCase.getCompanyData(corpCode);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "corpCode", corpCode,
                    "data", data
            ));
        } catch (Exception e) {
            log.error("회사 조회 실패", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "조회 실패: " + e.getMessage()
            ));
        }
    }

    /** 접수번호별 조회 */
    @GetMapping("/receipt/{rceptNo}")
    public ResponseEntity<?> getDataByRceptNo(@PathVariable String rceptNo) {
        try {
            Map<String, Object> data = (Map<String, Object>) queryUseCase.getDataByRceptNo(rceptNo);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "rceptNo", rceptNo,
                    "data", data
            ));
        } catch (Exception e) {
            log.error("접수번호 조회 실패", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "조회 실패: " + e.getMessage()
            ));
        }
    }

    /** 테스트 */
    @PostMapping("/test")
    public ResponseEntity<?> testApi() {
        try {
            String testCorpCode = "00126380";
            String beginDe = "20220101";
            String endDe = "20221231";
            fetchUseCase.fetchAndSaveSecuritiesData(testCorpCode, beginDe, endDe);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "테스트 수집 완료"
            ));
        } catch (Exception e) {
            log.error("테스트 실패", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "테스트 실패: " + e.getMessage()
            ));
        }
    }
}
