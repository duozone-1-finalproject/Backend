package com.example.finalproject.apitest.service.impl;

import com.example.finalproject.apitest.config.DartApiProperties;
import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartMajorShareholderStatusItem;
import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderStatusResponse;
import com.example.finalproject.apitest.entity.periodic.DartMajorShareholderStatus;
import com.example.finalproject.apitest.repository.periodic.DartMajorShareholderStatusRepository;
import com.example.finalproject.apitest.service.TestService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

    private final RestClient client;
    private final ObjectMapper objectMapper;
    private final DartMajorShareholderStatusRepository dartMajorShareholderStatusRepository;

    List<String> reprtCodeList=new ArrayList<>(List.of("11011","11012","11013","11014"));

    @Value("${dart.api.key}")
    private String dartApiKey;

    @Value("${dart.api.base-url}")
    private String baseUrl;

    public TestServiceImpl(DartApiProperties dartApiProperties, ObjectMapper objectMapper,  DartMajorShareholderStatusRepository dartMajorShareholderStatusRepository){
        this.client = RestClient.builder()
                .baseUrl(dartApiProperties.getBaseUrl())
                .build();
        this.objectMapper = objectMapper;
        this.dartMajorShareholderStatusRepository = dartMajorShareholderStatusRepository;

    }

    @Override
    @Transactional
    public List<DartMajorShareholderStatusResponse> testServ(String corpCode, String bsnsYear, String reprtCode) {

        // 1. 반환받을 정확한 제네릭 타입을 정의합니다.
        ParameterizedTypeReference<DartApiResponseDto<DartMajorShareholderStatusItem>> responseType =
                new ParameterizedTypeReference<>() {};

        DartApiResponseDto<DartMajorShareholderStatusItem> responseDto;
        try {
            // 2. API 호출 시 body()에 정의한 타입을 넘겨줍니다.
            responseDto = client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/hyslrSttus.json")
                            .queryParam("crtfc_key", dartApiKey)
                            .queryParam("corp_code", corpCode)
                            .queryParam("bsns_year", bsnsYear)
                            .queryParam("reprt_code", reprtCode)
                            .build())
                    .retrieve()
                    .body(responseType); // ⬅️ ParameterizedTypeReference 사용
        } catch (Exception e) {
            log.error("DART API 호출 중 에러 발생", e);
            return Collections.emptyList();
        }

        // 3. 응답 DTO의 상태를 확인합니다.
        if (responseDto == null || !"000".equals(responseDto.getStatus())) {
            log.warn("DART API 에러: status={}, message={}",
                    responseDto != null ? responseDto.getStatus() : "null",
                    responseDto != null ? responseDto.getMessage() : "null response");
            return Collections.emptyList();
        }

        // 4. DTO에서 직접 'list'를 가져옵니다.
        List<DartMajorShareholderStatusItem> items = responseDto.getList();
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartMajorShareholderStatus> entitiesToSave = new ArrayList<>();
        for (DartMajorShareholderStatusItem item : items) {
            if ("계".equals(item.getNm())) continue;
            log.info("stlmDt : {}", item.getStlmDt());
            // 5. API DTO를 DB Entity로 변환합니다.
            DartMajorShareholderStatus shareholder = DartMajorShareholderStatus.builder()
                    .rceptNo(item.getRceptNo())
                    .corpCls(item.getCorpCls())
                    .corpCode(item.getCorpCode())
                    .corpName(item.getCorpName())
                    .nm(item.getNm())
                    .relate(item.getRelate())
                    .stockKnd(item.getStockKnd())
                    .bsisPosesnStockCo(safeParseLong(item.getBsisPosesnStockCo()))
                    .bsisPosesnStockQotaRt(safeParseDouble(item.getBsisPosesnStockQotaRt()))
                    .trmendPosesnStockCo(safeParseLong(item.getTrmendPosesnStockCo()))
                    .trmendPosesnStockQotaRt(safeParseDouble(item.getTrmendPosesnStockQotaRt()))
                    .rm(item.getRm())
                    .stlmDt(safeParseLocalDate(item.getStlmDt()))
                    .build();
            entitiesToSave.add(shareholder);
        }
        log.info("123");

        // 6. 변환된 Entity를 DB에 저장하고, 저장된 결과를 받습니다.
        if (!entitiesToSave.isEmpty()) {
            log.info("4");
            List<DartMajorShareholderStatus> savedEntities = dartMajorShareholderStatusRepository.saveAll(entitiesToSave);
            log.info("5");
            // 7. 저장된 Entity 리스트를 Response DTO 리스트로 변환하여 반환합니다.
            return savedEntities.stream()
                    .map(DartMajorShareholderStatusResponse::from)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }



    // 스트링 안전한 형변환기 -------------------------------------------------------------------------------------------
    // 안전하게 long 타입으로 변환
    private Long safeParseLong(String textValue) {
        if (!StringUtils.hasText(textValue) || "-".equals(textValue)) {
            return null;
        }
        try {
            return Long.parseLong(textValue.replace(",", ""));
        } catch (NumberFormatException e) {
            log.warn("Long 타입으로 변환할 수 없는 값입니다: {}", textValue);
            return null;
        }
    }

    // 안전하게 double 타입으로 변환
    private Double safeParseDouble(String textValue) {
        if (!StringUtils.hasText(textValue) || "-".equals(textValue)) {
            return null;
        }
        try {
            return Double.parseDouble(textValue.replace(",", ""));
        } catch (NumberFormatException e) {
            log.warn("Double 타입으로 변환할 수 없는 값입니다: {}", textValue);
            return null;
        }
    }

    // 안전하게 LocalDate 타입으로 변환
    private LocalDate safeParseLocalDate(String textValue) {
        if (!StringUtils.hasText(textValue)) {
            return null;
        }
        try {
            return LocalDate.parse(textValue);
        } catch (DateTimeParseException e) {
            log.warn("LocalDate 타입으로 변환할 수 없는 값입니다: {}", textValue);
            return null;
        }
    }
    // --------------------------------------------------------------------------------------------------------------
}
