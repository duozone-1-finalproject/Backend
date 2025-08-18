package com.example.finalproject.dart.service.impl;

import com.example.finalproject.dart.dto.CompanyOverview.CompanyOverviewListResponseDto;
import com.example.finalproject.dart.dto.dart.DartReportListResponseDto;
import com.example.finalproject.dart.dto.dart.DartDocumentListRequestDto;
import com.example.finalproject.dart.dto.dart.DownloadAllRequestDto;
import com.example.finalproject.dart.service.DartApiService;
import com.example.finalproject.dart.service.DbService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class DartApiServiceImpl implements DartApiService {

    private final WebClient dartWebClient;
    private final DbService dbService;

    @Value("${dart.api.key}")
    private String dartApiKey;

    public DartApiServiceImpl(WebClient.Builder webClientBuilder,
                              @Value("${dart.api.base-url:https://opendart.fss.or.kr}") String baseUrl,
                              DbService dbService) {
        this.dartWebClient = webClientBuilder.baseUrl(baseUrl).build();
        this.dbService = dbService;
    }

    @Override
    public Mono<DartReportListResponseDto> findByCorpCodeAndReportName(DartDocumentListRequestDto dto) {
        return dartWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/list.json")
                        .queryParam("crtfc_key", dartApiKey)
                        .queryParam("corp_code", dto.getCorpCode())
                        .queryParam("bgn_de", dto.getBgnDe())
                        .queryParam("end_de", dto.getEndDe())
                        .queryParam("page_count", "100")
                        .build())
                .retrieve()
                .bodyToMono(DartReportListResponseDto.class)
                .map(resDto -> {
                    List<DartReportListResponseDto.ReportSummary> filtered = Optional.ofNullable(resDto.getList())
                            .orElse(Collections.emptyList())
                            .stream()
                            .filter(doc -> {
                                String name = doc.getReportNm();
                                return name != null && name.contains(dto.getReportNm());
                            })
                            .collect(Collectors.toList());
                    resDto.setList(filtered);
                    return resDto;
                });
    }

    public Mono<ResponseEntity<Resource>> downloadDocumentByCode(String rceptNo) {
        return dartWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/document.xml")
                        .queryParam("crtfc_key", dartApiKey)
                        .queryParam("rcept_no", rceptNo)
                        .build())
                .retrieve()
                .bodyToMono(ByteArrayResource.class)
                .map(resource -> {
                    try (ByteArrayInputStream bais = new ByteArrayInputStream(resource.getByteArray());
                         ZipInputStream zis = new ZipInputStream(bais);
                         ByteArrayOutputStream os = new ByteArrayOutputStream()) {

                        ZipEntry entry;
                        while ((entry = zis.getNextEntry()) != null) {
                            if (entry.getName().endsWith(".xml")) {
                                byte[] buf = new byte[1024];
                                int len;
                                while ((len = zis.read(buf)) > 0) os.write(buf, 0, len);
                                zis.closeEntry();
                                break;
                            }
                        }

                        ByteArrayResource xmlResource = new ByteArrayResource(os.toByteArray());
                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + rceptNo + ".xml\"")
                                .header(HttpHeaders.CONTENT_TYPE, "application/xml")
                                .body(xmlResource);

                    } catch (IOException e) {
                        throw new RuntimeException("압축 해제 중 오류 발생", e);
                    }
                });
    }

    // ==== 이하 로직은 그대로 ====
    public List<String> dbLoadAllCorpCode(DownloadAllRequestDto dto) {
        CompanyOverviewListResponseDto companyInfo = dbService.getAllCompanyOverviews();
        return companyInfo.getCompanies().stream()
                .map(company -> company.getCorpCode())
                .collect(Collectors.toList());
    }

    public List<String> getRceptNos(DartReportListResponseDto dto) {
        return Optional.ofNullable(dto.getList()).orElse(Collections.emptyList())
                .stream()
                .map(DartReportListResponseDto.ReportSummary::getRceptNo)
                .collect(Collectors.toList());
    }

    public DartReportListResponseDto getCompanyInfoByCorpCode(String corpCode, DownloadAllRequestDto dto) {
        return dartWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/list.json")
                        .queryParam("crtfc_key", dartApiKey)
                        .queryParam("corp_code", corpCode)
                        .queryParam("bgn_de", dto.getBgnDe())
                        .queryParam("end_de", dto.getEndDe())
                        .queryParam("page_count", "100")
                        .build())
                .retrieve()
                .bodyToMono(DartReportListResponseDto.class)
                .map(resDto -> {
                    System.out.println("응답 메시지: " + resDto.getMessage());
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
                .doOnError(e -> System.out.println("API 호출 실패: " + e.getMessage()))
                .block();
    }

    public String downReports(List<String> rceptNos, DownloadAllRequestDto dto) {
        String apiKey = dartApiKey;
        String baseUrl = "https://opendart.fss.or.kr/api/document.xml";
        String saveDir = "C:/" + dto.getReportNm();

        RestTemplate restTemplate = new RestTemplate();
        File directory = new File(saveDir);
        if (!directory.exists()) directory.mkdirs();

        int success = 0;
        for (String rceptNo : rceptNos) {
            try {
                String url = baseUrl + "?crtfc_key=" + apiKey + "&rcept_no=" + rceptNo;
                ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    byte[] zipData = response.getBody();
                    try (ByteArrayInputStream bais = new ByteArrayInputStream(zipData);
                         ZipInputStream zis = new ZipInputStream(bais)) {
                        ZipEntry entry;
                        while ((entry = zis.getNextEntry()) != null) {
                            if (entry.getName().endsWith(".xml")) {
                                File out = new File(saveDir + "/" + rceptNo + ".xml");
                                try (FileOutputStream fos = new FileOutputStream(out)) {
                                    byte[] buf = new byte[1024]; int len;
                                    while ((len = zis.read(buf)) > 0) fos.write(buf, 0, len);
                                }
                                break;
                            }
                        }
                    }
                    success++;
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
        return "다운로드 완료: " + success + "/" + rceptNos.size() + "건(성공수/보고서수)\n";
    }

    public String downReportsDir(List<String> rceptNos, DownloadAllRequestDto dto) {
        String apiKey = dartApiKey;
        String baseUrl = "https://opendart.fss.or.kr/api/document.xml";
        String saveDir = "C:/" + dto.getReportNm();

        RestTemplate restTemplate = new RestTemplate();
        File directory = new File(saveDir);
        if (!directory.exists()) directory.mkdirs();

        int success = 0;
        for (String rceptNo : rceptNos) {
            try {
                String url = baseUrl + "?crtfc_key=" + apiKey + "&rcept_no=" + rceptNo;
                ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    byte[] zipData = response.getBody();
                    try (ByteArrayInputStream bais = new ByteArrayInputStream(zipData);
                         ZipInputStream zis = new ZipInputStream(bais)) {
                        File rceptDir = new File(saveDir + "/" + rceptNo);
                        if (!rceptDir.exists()) rceptDir.mkdirs();

                        ZipEntry entry;
                        while ((entry = zis.getNextEntry()) != null) {
                            File out = new File(rceptDir, entry.getName());
                            File parent = out.getParentFile();
                            if (parent != null && !parent.exists()) parent.mkdirs();
                            try (FileOutputStream fos = new FileOutputStream(out)) {
                                byte[] buf = new byte[1024]; int len;
                                while ((len = zis.read(buf)) > 0) fos.write(buf, 0, len);
                            }
                        }
                    }
                    success++;
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
        return "다운로드 완료: " + success + "/" + rceptNos.size() + "건(성공수/보고서수)\n";
    }

    public String processAllCompanies(List<String> corpCodes, DownloadAllRequestDto dto) {
        int totalProcessed = 0;
        for (String corpCode : corpCodes) {
            try {
                DartReportListResponseDto companyInfo = getCompanyInfoByCorpCode(corpCode, dto);
                if (companyInfo == null || companyInfo.getList() == null || companyInfo.getList().isEmpty()) {
                    System.out.println("[" + corpCode + "] 관련 보고서 없음");
                    continue;
                }
                List<String> rceptNos = getRceptNos(companyInfo);
                String msg = downReportsDir(rceptNos, dto);
                System.out.println("[" + corpCode + "] 다운로드 결과 → " + msg);
                totalProcessed++;
                Thread.sleep(1000);
            } catch (Exception e) {
                System.err.println("회사 코드 [" + corpCode + "] 처리 중 오류: " + e.getMessage());
            }
        }
        System.out.println("총 처리 완료 기업 수: " + totalProcessed + "/" + corpCodes.size());
        return "총 처리 완료 기업 수: " + totalProcessed + "/" + corpCodes.size();
    }

    public String saveDownloadedReports(DownloadAllRequestDto dto) {
        List<String> allCorpCode = dbLoadAllCorpCode(dto);
        return processAllCompanies(allCorpCode, dto);
    }
}
