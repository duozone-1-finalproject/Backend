package com.example.finalproject.dart.service.impl;

import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.finalproject.dart.dto.dart.*;
import com.example.finalproject.dart.exception.BusinessReportException;
import com.example.finalproject.dart.service.DartApiService;
import com.example.finalproject.dart.service.DbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Slf4j
@Service
public class DartApiServiceImpl implements DartApiService {

    private final WebClient dartWebClient;
    private final DbService dbService;
    private final RestClient fastApiClient;

    @Value("${dart.api.key}")
    private String dartApiKey;

    public DartApiServiceImpl(@Qualifier("dartWebClient") WebClient dartWebClient, @Qualifier("fastApiClient") RestClient fastApiClient, DbService dbService) {
        this.dartWebClient = dartWebClient;
        this.dbService = dbService; // dbService 초기화
        this.fastApiClient = fastApiClient;
    }

    // 기업코드와 문서제목으로 보고서 검색
    @Override
    public Mono<DartReportListResponseDto> findByCorpCodeAndReportName(DartDocumentListRequestDto dto) {
        // 보고서 리스트 가져오는 api
        //http://localhost:8080/api/dart/reports?corpCode=00172291&reportNm=보고서&bgnDe=20200101&endDe=20241111
        // 참고 링크 https://opendart.fss.or.kr/guide/detail.do?apiGrpCd=DS001&apiId=2019001
        return dartWebClient.get()
                .uri("/api/list.json?"+
                        "crtfc_key="+dartApiKey+
                        "&corp_code="+dto.getCorpCode()+    // ex) "00172291" 8자리의 기업고유코드
                        "&bgn_de="+dto.getBgnDe()+          // ex) "20200101" 8자리의 시작날짜
                        "&end_de="+dto.getEndDe()+          // ex) "20241111" 8자리의 종료날짜

                        "&page_count="+"100")               // 페이지 로드의 최대 수
                .retrieve()
                .bodyToMono(DartReportListResponseDto.class)   // 여기까지가 dart 응답의 원본
                .map(resDto -> {         // 검색어를 이용하여 가져온 보고서리스트 서치
                    // "검색 키워드"가 포함된 항목만 필터링
                    List<DartReportListResponseDto.ReportSummary> filtered = resDto.getList().stream()
                            .filter(doc -> doc.getReportNm().contains(dto.getReportNm())) // 여기에 검색 키워드 넣기
                            .collect(Collectors.toList());

                    resDto.setList(filtered); // 필터링된 리스트로 대체
                    return resDto;
                });
    }
/*
    public Mono<ResponseEntity<Resource>> downloadDocumentByCode(String rceptNo){
        return dartWebClient.get()
                .uri("/api/document.xml?"+
                        "crtfc_key="+dartApiKey+
                        "&rcept_no="+rceptNo)       // 다운로드할 문서의 고유코드(문서의 접수번호)
                .retrieve()
                .bodyToMono(ByteArrayResource.class)
                .map(resource -> {
                    String fakeZipFileName = "report.zip";

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fakeZipFileName + "\"")
                            .header(HttpHeaders.CONTENT_TYPE, "application/zip")  // 실제 내용은 xml이지만 이름만 zip
                            .body(resource);
                });
    }

 */

    // 보고서.xml 다운로드
    public Mono<ResponseEntity<Resource>> downloadDocumentByCode(String rceptNo) {
        // 참고 링크 https://opendart.fss.or.kr/guide/detail.do?apiGrpCd=DS001&apiId=2019003
        // 보고서 다운로드 api
        // http://localhost:8080/api/dart/download?rceptNo=20241024000371
        return dartWebClient.get()
                .uri("/api/document.xml?" +
                        "crtfc_key=" + dartApiKey +
                        "&rcept_no=" + rceptNo)
                .retrieve()
                .bodyToMono(ByteArrayResource.class)
                .map(resource -> {
                    try (
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(resource.getByteArray());
                            ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
                    ) {
                        ZipEntry entry;
                        while ((entry = zipInputStream.getNextEntry()) != null) {
                            if (entry.getName().endsWith(".xml")) {
                                // .xml 파일을 읽어 outputStream에 복사
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = zipInputStream.read(buffer)) > 0) {
                                    outputStream.write(buffer, 0, len);
                                }
                                zipInputStream.closeEntry();
                                break; // 첫 번째 .xml 파일만 처리
                            }
                        }

                        ByteArrayResource xmlResource = new ByteArrayResource(outputStream.toByteArray());

                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + rceptNo + ".xml\"")
                                .header(HttpHeaders.CONTENT_TYPE, "application/xml")
                                .body(xmlResource);

                    } catch (IOException e) {
                        throw new RuntimeException("압축 해제 중 오류 발생", e);
                    }
                });
    }
/*
    private String reportNm; // 보고서 이름
    private String bgnDe; // 시작날짜
    private String endDe; // 종료날짜
 */

// 이 아래가 지옥의 api 크롤링...-----------------------------------------------------------------------------------------------

    // DB에 있는 기업정보에서 회사코드(corpCode) 전부 가져오기
    public List<String> dbLoadAllCorpCode(DownloadAllRequestDto dto){
        CompanyOverviewListResponseDto companyInfo=dbService.getAllCompanyOverviews();

        // 기업 코드가 전부 저장되어있음
        List<String> corpCodeList = companyInfo.getCompanies().stream()
                .map(company -> company.getCorpCode())
                .collect(Collectors.toList());
        log.error("dbLoadAllCorpCode size : "+String.valueOf(corpCodeList.size()));
        return corpCodeList;
    }

    // 해당 회사 보고서의 접수번호리스트 반환
    public List<String> getRceptNos(DartReportListResponseDto dto) {
        return dto.getList().stream()
                .map(DartReportListResponseDto.ReportSummary::getRceptNo)
                .collect(Collectors.toList());
    }

    // 회사코드(corpCode)를 통해서 api로 (회사정보,(보고서리스트))를 가져옴
    public DartReportListResponseDto getCompanyInfoByCorpCode(String corpCode, DownloadAllRequestDto dto){
        DartReportListResponseDto result = dartWebClient.get()
                .uri("/api/list.json?" +
                        "crtfc_key=" + dartApiKey +
                        "&corp_code=" + corpCode +
                        "&bgn_de=" + dto.getBgnDe() +
                        "&end_de=" + dto.getEndDe() +
                        "&page_count=100")
                .retrieve()
                .bodyToMono(DartReportListResponseDto.class)
                .map(resDto -> {
                    System.out.println("응답 메시지: " + resDto.getMessage()); // 응답 메시지 출력 (성공 시)

                    List<DartReportListResponseDto.ReportSummary> filtered = Optional.ofNullable(resDto.getList())
                            .orElse(Collections.emptyList())
                            .stream()
                            .filter(doc -> {
                                String reportName = doc.getReportNm();
                                return reportName != null && reportName.contains(dto.getReportNm());
                            })
                            .collect(Collectors.toList());

                    resDto.setList(filtered);
                    return resDto;
                })
                .doOnError(e -> System.out.println("API 호출 실패: " + e.getMessage())) // 실패 메시지
                .block(); // 여기서 동기적으로 받아옴

        return result;
    }

    // 접수번호(rceptNos)에 맞는 보고서(.xml)를 검색명(reportNm)폴더에 저장
    public String downReports(List<String> rceptNos, DownloadAllRequestDto dto) {
        String apiKey = dartApiKey; // dartApiKey
        String baseUrl = "https://opendart.fss.or.kr/api/document.xml";
        String saveDir = "C:/"+dto.getReportNm(); // 저장할 로컬 폴더 경로

        RestTemplate restTemplate = new RestTemplate();

        // 저장 폴더가 없다면 생성
        File directory = new File(saveDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        int rceptNosCount=0;
        for (String rceptNo : rceptNos) {
            try {
                String url = baseUrl + "?crtfc_key=" + apiKey + "&rcept_no=" + rceptNo;

                // zip 응답 받아오기
                ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    byte[] zipData = response.getBody();

                    // 압축 해제
                    try (
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipData);
                            ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)
                    ) {
                        ZipEntry entry;
                        while ((entry = zipInputStream.getNextEntry()) != null) {
                            if (entry.getName().endsWith(".xml")) {
                                File outputFile = new File(saveDir + "/" + rceptNo + ".xml");
                                try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                                    byte[] buffer = new byte[1024];
                                    int len;
                                    while ((len = zipInputStream.read(buffer)) > 0) {
                                        fileOutputStream.write(buffer, 0, len);
                                    }
                                }
                                break; // 첫 번째 xml만 저장
                            }
                        }
                    }
                    rceptNosCount++;
                } else {
                    System.err.println("Failed to download for rceptNo: " + rceptNo);
                }

                // 1초 대기
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt(); // 인터럽트 상태 복원
                System.err.println("Thread interrupted during sleep.");
            } catch (Exception e) {
                System.err.println("Error processing rceptNo: " + rceptNo + ", " + e.getMessage());
            }
        }

        return "다운로드 완료: " +rceptNosCount+ "/"+rceptNos.size() + "건(성공수/보고서수)\n";
    }

    // 접수번호(rceptNos)에 맞는 압축파일을 폴더명으로 압축해제해서 검색명(reportNm)폴더에 저장
    public String downReportsDir(List<String> rceptNos, DownloadAllRequestDto dto) {
        String apiKey = dartApiKey; // dartApiKey
        String baseUrl = "https://opendart.fss.or.kr/api/document.xml";
        String saveDir = "C:/"+dto.getReportNm(); // 저장할 로컬 폴더 경로

        RestTemplate restTemplate = new RestTemplate();

        // 저장 폴더가 없다면 생성
        File directory = new File(saveDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        int rceptNosCount=0;
        for (String rceptNo : rceptNos) {
            try {
                String url = baseUrl + "?crtfc_key=" + apiKey + "&rcept_no=" + rceptNo;
                ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    byte[] zipData = response.getBody();

                    try (
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipData);
                            ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)
                    ) {
                        // 1. rceptNo 디렉토리 생성: 예) C:/증권신고서/20240725001234
                        File rceptDir = new File(saveDir + "/" + rceptNo);
                        if (!rceptDir.exists()) {
                            rceptDir.mkdirs();
                        }

                        // 2. zip 안 모든 파일 저장
                        ZipEntry entry;
                        while ((entry = zipInputStream.getNextEntry()) != null) {
                            String entryFileName = entry.getName(); // 예: D0001.xml
                            File outputFile = new File(rceptDir, entryFileName);

                            // 하위 디렉토리도 고려
                            File parentDir = outputFile.getParentFile();
                            if (parentDir != null && !parentDir.exists()) {
                                parentDir.mkdirs();
                            }

                            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = zipInputStream.read(buffer)) > 0) {
                                    fileOutputStream.write(buffer, 0, len);
                                }
                            }
                        }
                    }
                    rceptNosCount++;
                    System.out.println("한글 테스트");
                    System.out.println("1234");
                } else {
                    System.err.println("Failed to download for rceptNo: " + rceptNo);
                }

                Thread.sleep(1000);

            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted during sleep.");
            } catch (Exception e) {
                System.err.println("Error processing rceptNo: " + rceptNo + ", " + e.getMessage());
            }
        }

        return "다운로드 완료: " +rceptNosCount+ "/"+rceptNos.size() + "건(성공수/보고서수)\n";
    }

    // 접수번호(rceptNos)에 맞는 압축파일을 폴더명으로 압축해제해서 검색명(corpCode/reportNm)폴더에 저장
    public String downReportsDirCompany(String corpCode, List<String> rceptNos, DownloadAllRequestDto dto) {
        String apiKey = dartApiKey; // dartApiKey
        String baseUrl = "https://opendart.fss.or.kr/api/document.xml";
        String saveDir = "C:/"+corpCode+"/"+dto.getReportNm(); // 저장할 로컬 폴더 경로

        RestTemplate restTemplate = new RestTemplate();

        // 저장 폴더가 없다면 생성
        File directory = new File(saveDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        int rceptNosCount=0;
        for (String rceptNo : rceptNos) {
            try {
                String url = baseUrl + "?crtfc_key=" + apiKey + "&rcept_no=" + rceptNo;
                ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    byte[] zipData = response.getBody();

                    try (
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipData);
                            ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)
                    ) {
                        // 1. rceptNo 디렉토리 생성: 예) C:/증권신고서/20240725001234
                        File rceptDir = new File(saveDir + "/"+ rceptNo);
                        if (!rceptDir.exists()) {
                            rceptDir.mkdirs();
                        }

                        // 2. zip 안 모든 파일 저장
                        ZipEntry entry;
                        while ((entry = zipInputStream.getNextEntry()) != null) {
                            String entryFileName = entry.getName(); // 예: D0001.xml
                            File outputFile = new File(rceptDir, entryFileName);

                            // 하위 디렉토리도 고려
                            File parentDir = outputFile.getParentFile();
                            if (parentDir != null && !parentDir.exists()) {
                                parentDir.mkdirs();
                            }

                            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = zipInputStream.read(buffer)) > 0) {
                                    fileOutputStream.write(buffer, 0, len);
                                }
                            }
                        }
                    }
                    rceptNosCount++;
                    System.out.println("한글 테스트");
                    System.out.println("1234");
                } else {
                    System.err.println("Failed to download for rceptNo: " + rceptNo);
                }

                Thread.sleep(1000);

            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted during sleep.");
            } catch (Exception e) {
                System.err.println("Error processing rceptNo: " + rceptNo + ", " + e.getMessage());
            }
        }

        return "다운로드 완료: " +rceptNosCount+ "/"+rceptNos.size() + "건(성공수/보고서수)\n";
    }

    // 회사코드리스트(corpCodes)의 회사코드별로 다운로드 반복 실행
    public String processAllCompanies(List<String> corpCodes, DownloadAllRequestDto dto) {
        int totalProcessed = 0;

        for (String corpCode : corpCodes) {
            try {
                // 회사정보 + 보고서리스트 가져오기
                DartReportListResponseDto companyInfo = getCompanyInfoByCorpCode(corpCode, dto);

                if (companyInfo == null || companyInfo.getList() == null || companyInfo.getList().isEmpty()) {
                    System.out.println("[" + corpCode + "] 관련 보고서 없음");
                    continue;
                }

                // 접수번호 리스트 추출
                List<String> rceptNos = getRceptNos(companyInfo);

                // 보고서 다운로드
                String resultMessage = downReportsDir(rceptNos, dto); // 이미 작성한 다운로드 메서드
                System.out.println("[" + corpCode + "] 다운로드 결과 → " + resultMessage);

                totalProcessed++;

                // 1초 대기
                Thread.sleep(1000);

            } catch (Exception e) {
                System.err.println("회사 코드 [" + corpCode + "] 처리 중 오류 발생: " + e.getMessage());
            }
        }

        System.out.println("총 처리 완료 기업 수: " + totalProcessed + "/" + corpCodes.size());
        return "총 처리 완료 기업 수: " + totalProcessed + "/" + corpCodes.size();
    }

    // 회사코드(corpCode)로 해당하는 보고서 전부 다운로드
    public String processCompany(String corpCode, DownloadAllRequestDto dto){

        try {
            // 회사정보 + 보고서리스트 가져오기
            DartReportListResponseDto companyInfo = getCompanyInfoByCorpCode(corpCode, dto);

            if (companyInfo == null || companyInfo.getList() == null || companyInfo.getList().isEmpty()) {
                System.out.println("[" + corpCode + "] 관련 보고서 없음");
            }

            // 접수번호 리스트 추출
            List<String> rceptNos = getRceptNos(companyInfo);

            // 보고서 다운로드
            String resultMessage = downReportsDirCompany(corpCode,rceptNos, dto); // 이미 작성한 다운로드 메서드
            System.out.println("[" + corpCode + "] 다운로드 결과 → " + resultMessage);

            // 1초 대기
            Thread.sleep(1000);

        } catch (Exception e) {
            System.err.println(corpCode + " fail: " + e.getMessage());
        }

        System.out.println("processCompany Sucess");
        return "processCompany Sucess";
    }

    // 위 함수를 통해 검색어명 폴더에 모든 기업의 검색된 보고서 저장
    @Override
    public String saveDownloadedReports(DownloadAllRequestDto dto){
        List<String> orpCodes=dbLoadAllCorpCode(dto);    // 모든 회사코드리스트
        return processAllCompanies(orpCodes, dto);
    }

    // 회사 하나의 보고서 전부 다운로드
    @Override
    public String saveDownloadedReportsCompany(String corpCode, DownloadAllRequestDto dto){
        return processCompany(corpCode, dto);
    }
// ----------------------------------------------------------------------------

    // 기업코드로 최근 1년간의 사업/1분기/3분기/반기/감사 보고서의 리스트 반환
    @Override
    public DartReportListResponseDto getRceptNosByCorpCode(String corpCode){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate today = LocalDate.now();
        String todayString = today.format(formatter);

        LocalDate oneYearAgo = today.minusYears(1);
        String oneYearAgoString = oneYearAgo.format(formatter);

        // 회사코드(corpCode)를 통해서 api로 (회사정보,(보고서리스트))를 가져옴
        DartReportListResponseDto rpt_biz=getCompanyInfoByCorpCode(corpCode, new DownloadAllRequestDto("사업",oneYearAgoString,todayString));
        DartReportListResponseDto rpt_qt=getCompanyInfoByCorpCode(corpCode, new DownloadAllRequestDto("분기",oneYearAgoString,todayString));
        DartReportListResponseDto rpt_half=getCompanyInfoByCorpCode(corpCode, new DownloadAllRequestDto("반기",oneYearAgoString,todayString));
        DartReportListResponseDto rpt_ad=getCompanyInfoByCorpCode(corpCode, new DownloadAllRequestDto("감사",oneYearAgoString,todayString));

        DartReportListResponseDto rpt_all=DartReportListResponseDto.builder()
                .status(rpt_biz.getStatus())
                .message(rpt_biz.getMessage()+"\n"+rpt_qt.getMessage()+"\n"+rpt_half.getMessage()+"\n"+rpt_ad.getMessage())
                .pageNo(rpt_biz.getPageNo())
                .pageCount(rpt_biz.getPageCount()+rpt_qt.getPageCount()+rpt_half.getPageCount()+rpt_ad.getPageCount())
                .totalCount(rpt_biz.getTotalCount()+rpt_qt.getTotalCount()+rpt_half.getTotalCount()+rpt_ad.getTotalCount())
                .totalPage(rpt_biz.getTotalPage())
                .list(Stream.of(rpt_biz.getList(), rpt_qt.getList(), rpt_half.getList(),rpt_ad.getList())
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .build();

        return rpt_all;
    }

    // 기업코드로 최근 5년간의 보고서의 리스트 반환
    @Override
    public DartReportListResponseDto getFiveYearRceptNosByCorpCode(String corpCode){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate today = LocalDate.now();
        String todayString = today.format(formatter);

        LocalDate fiveYearAgo = today.minusYears(5);
        String fiveYearAgoString = fiveYearAgo.format(formatter);

        // 회사코드(corpCode)를 통해서 api로 (회사정보,(보고서리스트))를 가져옴(최대 100개)
        return getCompanyInfoByCorpCode(corpCode, new DownloadAllRequestDto("사업",fiveYearAgoString,todayString));
    }

    // 기업코드로 최신 사업보고서 접수번호 반환
    public String getLatestBusinessReportCodeByCorpCode(String corpCode){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate today = LocalDate.now();
        String todayString = today.format(formatter);

        LocalDate twoYearAgo = today.minusYears(2);
        String twoYearAgoString = twoYearAgo.format(formatter);

        DartReportListResponseDto list = getCompanyInfoByCorpCode(corpCode, new DownloadAllRequestDto("사업", twoYearAgoString, todayString));

        // 응답이 null이거나 리스트가 비어있는 경우 처리
        if (list == null || list.getList() == null || list.getList().isEmpty()) {
            return null; // 또는 예외를 던지거나 기본값 반환
        }

        // rcept_dt(접수일자) 기준으로 가장 최신 사업보고서의 접수번호 반환
        return list.getList().stream()
                .filter(report -> report.getReportNm() != null && report.getReportNm().contains("사업"))
                .filter(report -> report.getRceptDt() != null) // 접수일자가 null이 아닌 것만
                .max((r1, r2) -> r1.getRceptDt().compareTo(r2.getRceptDt())) // 접수일자로 정렬
                .map(DartReportListResponseDto.ReportSummary::getRceptNo) // 접수번호 추출
                .orElse(null); // 결과가 없으면 null 반환
    }

    // 기업코드로 최신 사업보고서 HTML 반환
    @Override
    public BusinessReportDto getLatestBusinessReportByCorpCode(String corpCode) throws BusinessReportException {
        // 1. 최신 사업보고서 접수번호 조회
        String rceptNo = getLatestBusinessReportCodeByCorpCode(corpCode);

        if (rceptNo == null) {
            log.warn("기업코드 {}에 대한 최신 사업보고서를 찾을 수 없습니다.", corpCode);
            throw new BusinessReportException("해당 기업코드에 대한 최신 사업보고서를 찾을 수 없습니다: " + corpCode);
        }

        log.info("기업코드 {}의 최신 사업보고서 접수번호: {}", corpCode, rceptNo);

        try {
            // 2. FastAPI 호출 시도 - HTML 파일 전체 조회
            String businessReportHtml = fastApiClient.get()
                    .uri("/search/file/{rceptNo}", rceptNo)
                    .retrieve()
                    .body(String.class);

            // 3. 성공 시: API 응답으로 HTML 반환
            log.info("Successfully fetched business report HTML from FastAPI for rceptNo: {}", rceptNo);
            return BusinessReportDto.builder()
                    .recepNo(rceptNo)
                    .htmlContent(businessReportHtml)
                    .build();

        } catch (RestClientException e) {
            // 4. 실패 시: 예외 발생
            log.error("Failed to fetch business report HTML from FastAPI for rceptNo: {}. Error: {}", rceptNo, e.getMessage());
            throw new BusinessReportException("사업보고서 조회에 실패했습니다. 접수번호: " + rceptNo + ", 오류: " + e.getMessage());
        }
    }

    // --------------------------------------------------------------------------------------------------------------------------
    // < 규상 코드 > -> saveFinancials 구현
    @Override
    public String saveFinancials(FinancialDto request) {
        try {
            Map<String, Object> result = fastApiClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/financials/")
                            .queryParam("corp_code", request.getCorpCode())
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});

            if (result != null) {
                return (String) result.get("message");
            } else {
                return "저장 실패";
            }

        } catch (RestClientException e) {
            log.error("FastAPI 재무 정보 저장 실패: corpCode={}, error={}", request.getCorpCode(), e.getMessage());
            return "재무 정보 저장 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}
/*
    1. xml 1개 다운하던걸 폴더 만들어서 내부 파일 전부 바꾸는걸로 전환
    2.

    try {
            // 1. API 호출 시도
            String section5Html = fastApiClient.get()
                    .uri("/search/file/{rceptNo}", "20240321000788")
                    .retrieve()
                    .body(String.class);

            // 2. 성공 시: API 응답으로 section5를 설정
            log.info("Successfully fetched section5 from FastAPI for rceptNo: {}", "20240321000788");
            newEntry.setSection5(section5Html);

        } catch (RestClientException e) {
            // 3. 실패 시: 로그를 남기고, 기존 DTO의 데이터로 section5를 설정 (Fallback)
            log.error("Failed to fetch section5 from FastAPI for rceptNo: {}. Falling back to DTO data. Error: {}", "20240321000788", e.getMessage());
            newEntry.setSection5(sections.get("section5"));
        }
*/




