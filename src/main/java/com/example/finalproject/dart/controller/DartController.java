package com.example.finalproject.dart.controller;


import com.example.finalproject.dart.dto.ApiResponse;
import com.example.finalproject.dart.dto.dart.*;
import com.example.finalproject.dart.exception.BusinessReportException;
import com.example.finalproject.dart.service.DartApiService;
import com.example.finalproject.dart.service.OtherNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dart")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class DartController {
    private final DartApiService dartApiService; // ✅ 여기서 생성자 주입 받기
    private final OtherNoticeService otherNoticeService;

    // [DartDocumentListRequestDto 내용]
    /*  [DartDocumentListRequestDto 내용]
        private Integer corpCode; // 기업코드
        private String reportNm; // 보고서 이름
        private Integer bgnDe; //시작날짜
        private Integer endDe; //종료날짜
    */
    // 문서 검색(보고서이름,기업코드,기간을 이용)
    @GetMapping("/reports")
    public Mono<DartReportListResponseDto> searchReports(@ModelAttribute DartDocumentListRequestDto dto){
        return dartApiService.findByCorpCodeAndReportName(dto);
    }

    // 보고서접수번호(rceptNo)를 통해 압축 해제된 보고서 다운로드
    @GetMapping("/documents/{rceptNo}/download")
    public Mono<ResponseEntity<Resource>> downloadDocumentByCode(@PathVariable String rceptNo){
        return dartApiService.downloadDocumentByCode(rceptNo);
    }

    @PostMapping("/download-all")
    public String downloadAllDocumentByKeyword(@RequestBody DownloadAllRequestDto dto){
        return dartApiService.saveDownloadedReports(dto);
    }

    // 기업코드로 최근 1년간의 사업/1분기/3분기/반기/감사 보고서의 리스트 반환
//    @GetMapping("/reports/core")
//    public DartReportListResponseDto test(@RequestParam("corp_code") String corpCode){
//        return dartApiService.getRceptNosByCorpCode(corpCode); // "01571107"
//    }

    // 기업코드로 최근 5년간의 모든 보고서의 리스트 반환
    @GetMapping("/reports/core")
    public DartReportListResponseDto fiveYearRceptCall(@RequestParam("corp_code") String corpCode){
        return dartApiService.getFiveYearRceptNosByCorpCode(corpCode); // "01571107"
    }

    // 기업코드로 최신 사업보고서의 접수번호 반환
    @GetMapping("/reports/latest")
    public ApiResponse<BusinessReportDto> getLatestBusinessReport(@RequestParam("corp_code") String corpCode) {
        try {
            BusinessReportDto result = dartApiService.getLatestBusinessReportByCorpCode(corpCode);
            return ApiResponse.success(result);
        } catch (BusinessReportException e) {
            log.error("사업보고서 조회 실패: {}", e.getMessage());
            return ApiResponse.fail(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage());
            return ApiResponse.fail("서버 내부 오류가 발생했습니다.");
        }
    }

    // 기업이름으로 기타사항 반환
    @GetMapping("/reports/etc-matters")
    public ApiResponse<String> getEtcMatters(@RequestParam("corp_name") String corpName){
        return ApiResponse.success(otherNoticeService.otherNotice(corpName));
    }

    // -------------------------------------------------------------------------------------- //
    // < 규상 코드 -> 재무정보 저장하는 controller (fin_corpCode OpenSearch에 저장)
    @PostMapping("/financials")
    public ApiResponse<String> saveFinancials(@RequestBody FinancialDto request) {
        return ApiResponse.success(dartApiService.saveFinancials(request));
    }




}
