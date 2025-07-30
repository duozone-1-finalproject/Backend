package com.example.finalproject.dart.controller;


import com.example.finalproject.dart.dto.dart.DartReportListResponseDto;
import com.example.finalproject.dart.dto.dart.DartDocumentListRequestDto;
import com.example.finalproject.dart.dto.dart.DownloadAllRequestDto;
import com.example.finalproject.dart.service.DartApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dart")
public class DartController {
    private final DartApiService dartApiService; // ✅ 여기서 생성자 주입 받기

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


}
