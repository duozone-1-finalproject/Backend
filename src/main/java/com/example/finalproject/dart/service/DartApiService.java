package com.example.finalproject.dart.service;

import com.example.finalproject.dart.dto.dart.DartReportListResponseDto;
import com.example.finalproject.dart.dto.dart.DartDocumentListRequestDto;
import com.example.finalproject.dart.dto.dart.DownloadAllRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface DartApiService {
    // 기업코드와 문서제목으로 보고서 검색
    Mono<DartReportListResponseDto> findByCorpCodeAndReportName(DartDocumentListRequestDto dto);

    // 보고서.xml 다운로드
    Mono<ResponseEntity<Resource>> downloadDocumentByCode(String documentCode);

    // 위 함수들을 통해 검색어명 폴더에 모든 기업의 검색된 보고서 저장
    String saveDownloadedReports(DownloadAllRequestDto dto);
}