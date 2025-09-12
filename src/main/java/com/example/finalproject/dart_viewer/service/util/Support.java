package com.example.finalproject.dart_viewer.service.util;

import com.example.finalproject.dart.dto.dart.DartReportListResponseDto;
import com.example.finalproject.dart.dto.dart.DownloadAllRequestDto;
import com.example.finalproject.dart.service.DartApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
public class Support {

    private final DartApiService dartApiService;

    // 기업코드로 최근 2년간의 사업보고서의 리스트 반환
    public DartReportListResponseDto getTwoYearRceptNosByCorpCode(String corpCode){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate today = LocalDate.now();
        String todayString = today.format(formatter);

        LocalDate twoYearAgo = today.minusYears(2);
        String twoYearAgoString = twoYearAgo.format(formatter);

        // 회사코드(corpCode)를 통해서 api로 (회사정보,(보고서리스트))를 가져옴(최대 100개)
        return dartApiService.getCompanyInfoByCorpCode(corpCode, new DownloadAllRequestDto("사업",twoYearAgoString,todayString));
    }

    // 기업코드로 가장 최신 사업보고서 반환
    public Optional<DartReportListResponseDto.ReportSummary> getLatestBusinessReport(String corpCode) {
        try {
            DartReportListResponseDto response = getTwoYearRceptNosByCorpCode(corpCode);

            if (response == null || response.getList() == null || response.getList().isEmpty()) {
                log.warn("기업코드 {}에 대한 사업보고서를 찾을 수 없습니다.", corpCode);
                return Optional.empty();
            }

            // rcept_dt(접수일자) 기준으로 가장 최신 보고서를 찾음
            Optional<DartReportListResponseDto.ReportSummary> latestReport = response.getList().stream()
                    .filter(report -> report.getReportNm() != null && report.getReportNm().contains("사업보고서"))
                    .max((r1, r2) -> {
                        String date1 = r1.getRceptDt();
                        String date2 = r2.getRceptDt();

                        // 날짜가 null인 경우 처리
                        if (date1 == null && date2 == null) return 0;
                        if (date1 == null) return -1;
                        if (date2 == null) return 1;

                        return date1.compareTo(date2);
                    });

            if (latestReport.isPresent()) {
                log.info("기업코드 {}의 최신 사업보고서를 찾았습니다: {} (접수일자: {})",
                        corpCode, latestReport.get().getReportNm(), latestReport.get().getRceptDt());
            } else {
                log.warn("기업코드 {}에 대한 사업보고서가 존재하지 않습니다.", corpCode);
            }

            return latestReport;

        } catch (Exception e) {
            log.error("기업코드 {}의 최신 사업보고서 조회 중 오류 발생", corpCode, e);
            return Optional.empty();
        }
    }

    // 기업코드로 가장 최신 사업보고서의 접수번호만 반환 (간편 메서드)
    public Optional<String> getLatestBusinessReportRceptNo(String corpCode) {
        return getLatestBusinessReport(corpCode)
                .map(DartReportListResponseDto.ReportSummary::getRceptNo);
    }
}