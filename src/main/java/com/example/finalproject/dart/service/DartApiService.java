package com.example.finalproject.dart.service;

import com.example.finalproject.dart.dto.dart.DartReportListResponseDto;
import com.example.finalproject.dart.dto.dart.DartDocumentListRequestDto;
import com.example.finalproject.dart.dto.dart.DownloadAllRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DartApiService {
    // 기업코드와 문서제목으로 보고서 검색
    Mono<DartReportListResponseDto> findByCorpCodeAndReportName(DartDocumentListRequestDto dto);
    // 보고서.xml 다운로드
    Mono<ResponseEntity<Resource>> downloadDocumentByCode(String documentCode);

// -----------------------------------------------------------------------------------------------
    // DB에 있는 기업정보에서 기업코드 전부 가져오기
    List<String> dbLoadAllCorpCode(DownloadAllRequestDto dto);
    // 해당 회사 보고서의 접수번호리스트 반환
    List<String> getRceptNos(DartReportListResponseDto dto);
    // 회사코드(corpCode)를 통해서 api로 (회사정보,(보고서리스트))를 가져옴
    DartReportListResponseDto getCompanyInfoByCorpCode(String corpCode, DownloadAllRequestDto dto);
    // 접수번호(rceptNos)에 맞는 보고서(.xml)를 검색명(reportNm)폴더에 저장
    String downReports(List<String> rceptNos, DownloadAllRequestDto dto);
    // 접수번호(rceptNos)에 맞는 압축파일을 폴더명으로 압축해제해서 검색명(reportNm)폴더에 저장
    String downReportsDir(List<String> rceptNos, DownloadAllRequestDto dto);
    // 회사코드리스트(corpCodes)의 회사코드별로 다운로드 실행
    String processAllCompanies(List<String> corpCodes, DownloadAllRequestDto dto);


    // 위 함수들을 통해 검색어명 폴더에 모든 기업의 검색된 보고서 저장
    String saveDownloadedReports(DownloadAllRequestDto dto);

    String saveDownloadedReportsCompany(String corpCode, DownloadAllRequestDto dto);

// -----------------------------------------------------------------------------------------------
    // 기업코드로 최근 1년간의 사업/1분기/3분기/반기/감사 보고서의 리스트 반환
    DartReportListResponseDto getRceptNosByCorpCode(String corpCode);


}
/*
1. 검색이 된것만 보내고 싶다.
2.
 */