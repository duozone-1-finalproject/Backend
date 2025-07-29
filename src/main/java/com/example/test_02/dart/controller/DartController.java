package com.example.test_02.dart.controller;


import com.example.dart.model.dto.dart.DartApiListResponseDto;
import com.example.dart.model.dto.dart.DartDocumentListRequestDto;
import com.example.dart.model.dto.dart.DownloadAllRequestDto;
import com.example.dart.model.service.DartApiService;
import com.example.dart.model.service.DartService;
import com.example.dart.model.service.impl.DartApiServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dart")
public class DartController {
    private final DartService dartService;
    private final DartApiService dartApiService; // ✅ 여기서 생성자 주입 받기

    // [DartDocumentListRequestDto 내용]
    /*  [DartDocumentListRequestDto 내용]
        private Integer corpCode; // 기업코드
        private String reportNm; // 보고서 이름
        private Integer bgnDe; //시작날짜
        private Integer endDe; //종료날짜
    */
    @GetMapping("/reports")
    public Mono<DartApiListResponseDto> searchReports(@ModelAttribute DartDocumentListRequestDto dto){
        return dartApiService.findByCorpCodeAndReportName(dto);
    }

    @GetMapping("/download")
    public Mono<ResponseEntity<Resource>> downloadDocumentByCode(@RequestParam String rceptNo){
        return dartApiService.downloadDocumentByCode(rceptNo);
    }

    @PostMapping("/download-all")
    public String downloadAllDocumentByKeyword(@RequestBody DownloadAllRequestDto dto){
        return dartApiService.saveDownloadedReports(dto);
    }


}
