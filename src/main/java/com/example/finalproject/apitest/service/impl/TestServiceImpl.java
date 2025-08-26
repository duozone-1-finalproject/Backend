package com.example.finalproject.apitest.service.impl;

import com.example.finalproject.apitest.config.DartApiProperties;
import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartExecutiveStatusItem;
import com.example.finalproject.apitest.dto.periodic.external.DartMajorShareholderChangeItem;
import com.example.finalproject.apitest.dto.periodic.external.DartMajorShareholderStatusItem;
import com.example.finalproject.apitest.dto.periodic.response.DartExecutiveStatusResponse;
import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderChangeResponse;
import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderStatusResponse;
import com.example.finalproject.apitest.entity.periodic.DartExecutiveStatus;
import com.example.finalproject.apitest.entity.periodic.DartMajorShareholderChange;
import com.example.finalproject.apitest.entity.periodic.DartMajorShareholderStatus;
import com.example.finalproject.apitest.exception.DartApiException;
import com.example.finalproject.apitest.repository.periodic.DartExecutiveStatusRepository;
import com.example.finalproject.apitest.repository.periodic.DartMajorShareholderChangeRepository;
import com.example.finalproject.apitest.repository.periodic.DartMajorShareholderStatusRepository;
import com.example.finalproject.apitest.service.TestService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.io.IOException;
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
    private final DartMajorShareholderChangeRepository dartMajorShareholderChangeRepository;
    private final DartExecutiveStatusRepository dartExecutiveStatusRepository;
    private final Support support;

    List<String> reprtCodeList=new ArrayList<>(List.of("11011","11012","11013","11014"));

    @Value("${dart.api.key}")
    private String dartApiKey;

    @Value("${dart.api.base-url}")
    private String baseUrl;

    public TestServiceImpl(DartApiProperties dartApiProperties,
                           ObjectMapper objectMapper,
                           DartMajorShareholderStatusRepository dartMajorShareholderStatusRepository,
                           DartMajorShareholderChangeRepository dartMajorShareholderChangeRepository,
                           DartExecutiveStatusRepository dartExecutiveStatusRepository
                           ){
        this.client = RestClient.builder()
                .baseUrl(dartApiProperties.getBaseUrl())
                .build();
        this.objectMapper = objectMapper;
        this.dartMajorShareholderStatusRepository = dartMajorShareholderStatusRepository;
        this.dartMajorShareholderChangeRepository = dartMajorShareholderChangeRepository;
        this.dartExecutiveStatusRepository=dartExecutiveStatusRepository;
        this.support = new Support();


    }

    @Override
    @Transactional
    public List<DartMajorShareholderStatusResponse> DartMajorShareholderStatusCall(String corpCode, String bsnsYear, String reprtCode) {
        // 최대주주 현황
        // 0. 데이터가 이미 존재하는지 확인합니다.
        if (!dartMajorShareholderStatusRepository.findByCorpCode(corpCode).isEmpty()){
            throw new DartApiException("이미 존재하는 데이터입니다. 다시 요청할 수 없습니다.");
        }

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
            throw new DartApiException("DART API 호출 중 에러 발생",e);
        }

        // 3. 응답 DTO의 상태를 확인합니다.
        if (responseDto == null || !"000".equals(responseDto.getStatus())) {
            throw new DartApiException("DART API 에러: status="
                    +(responseDto != null ? responseDto.getStatus() : "null")
                    +", message="
                    +(responseDto != null ? responseDto.getMessage() : "null response"));
        }

        // 4. DTO에서 직접 'list'를 가져옵니다.
        List<DartMajorShareholderStatusItem> items = responseDto.getList();
        if (items == null || items.isEmpty()) {
            log.info("DART API로부터 수신한 데이터가 없습니다.");
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
                    .bsisPosesnStockCo(support.safeParseLong(item.getBsisPosesnStockCo()))
                    .bsisPosesnStockQotaRt(support.safeParseDouble(item.getBsisPosesnStockQotaRt()))
                    .trmendPosesnStockCo(support.safeParseLong(item.getTrmendPosesnStockCo()))
                    .trmendPosesnStockQotaRt(support.safeParseDouble(item.getTrmendPosesnStockQotaRt()))
                    .rm(item.getRm())
                    .stlmDt(support.safeParseLocalDate(item.getStlmDt()))
                    .build();
            entitiesToSave.add(shareholder);
        }

        // 6. 변환된 Entity를 DB에 저장하고, 저장된 결과를 받습니다.
        if (!entitiesToSave.isEmpty()) {
            List<DartMajorShareholderStatus> savedEntities = dartMajorShareholderStatusRepository.saveAll(entitiesToSave);
            // 7. 저장된 Entity 리스트를 Response DTO 리스트로 변환하여 반환합니다.
            return savedEntities.stream()
                    .map(DartMajorShareholderStatusResponse::from)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional
    public List<DartMajorShareholderChangeResponse> DartMajorShareholderChangeCall(String corpCode, String bsnsYear, String reprtCode) throws IOException{
        // 최대주주 변동현황
        // 0. 데이터가 이미 존재하는지 확인합니다.
        if (!dartMajorShareholderChangeRepository.findByCorpCode(corpCode).isEmpty()){
            throw new DartApiException("이미 존재하는 데이터입니다. 다시 요청할 수 없습니다.");
        }

        // 1. 반환받을 정확한 제네릭 타입을 정의합니다.
        ParameterizedTypeReference<DartApiResponseDto<DartMajorShareholderChangeItem>> responseType =
                new ParameterizedTypeReference<>() {};

        DartApiResponseDto<DartMajorShareholderChangeItem> responseDto;
        try {
            // 2. API 호출 시 body()에 정의한 타입을 넘겨줍니다.
            responseDto = client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/hyslrChgSttus.json")
                            .queryParam("crtfc_key", dartApiKey)
                            .queryParam("corp_code", corpCode)
                            .queryParam("bsns_year", bsnsYear)
                            .queryParam("reprt_code", reprtCode)
                            .build())
                    .retrieve()
                    .body(responseType); // ⬅️ ParameterizedTypeReference 사용
        } catch (Exception e) {
            throw new DartApiException("DART API 호출 중 에러 발생",e);
        }

        // 3. 응답 DTO의 상태를 확인합니다.
        if (responseDto == null || !"000".equals(responseDto.getStatus())) {
            throw new DartApiException("DART API 에러: status="
                    +(responseDto != null ? responseDto.getStatus() : "null")
                    +", message="
                    +(responseDto != null ? responseDto.getMessage() : "null response"));
        }

        // 4. DTO에서 직접 'list'를 가져옵니다.
        List<DartMajorShareholderChangeItem> items = responseDto.getList();
        if (items == null || items.isEmpty()) {
            log.info("DART API로부터 수신한 데이터가 없습니다.");
            return Collections.emptyList();
        }

        List<DartMajorShareholderChange> entitiesToSave = new ArrayList<>();
        for (DartMajorShareholderChangeItem item : items) {
            // 최대주주 변동현황 API에는 '계'와 같은 합산 행이 없으므로, 필터링 로직은 필요 없습니다.
            log.info("changeOn : {}", item.getChangeOn());

            // 5. API DTO를 DB Entity로 변환합니다.
            DartMajorShareholderChange shareholderChange = DartMajorShareholderChange.builder()
                    .rceptNo(item.getRceptNo())
                    .corpCls(item.getCorpCls())
                    .corpCode(item.getCorpCode())
                    .corpName(item.getCorpName())
                    .changeOn(support.safeParseLocalDate(item.getChangeOn(), "yyyy.MM.dd")) // 날짜 형식이 다르므로 패턴 지정
                    .mxmmShrhldrNm(item.getMxmmShrhldrNm())
                    .posesnStockCo(support.safeParseLong(item.getPosesnStockCo()))
                    .qotaRt(support.safeParseDouble(item.getQotaRt()))
                    .changeCause(item.getChangeCause())
                    .rm(item.getRm())
                    .stlmDt(support.safeParseLocalDate(item.getStlmDt())) // 기본 "yyyy-MM-dd" 형식
                    .build();
            entitiesToSave.add(shareholderChange);
        }

        // 6. 변환된 Entity를 DB에 저장하고, 저장된 결과를 받습니다.
        if (!entitiesToSave.isEmpty()) {
            List<DartMajorShareholderChange> savedEntities = dartMajorShareholderChangeRepository.saveAll(entitiesToSave);
            // 7. 저장된 Entity 리스트를 Response DTO 리스트로 변환하여 반환합니다.
            return savedEntities.stream()
                    .map(DartMajorShareholderChangeResponse::from)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional
    public List<DartExecutiveStatusResponse> DartExecutiveStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException{
        // 임원 현황
        // 0. 데이터가 이미 존재하는지 확인합니다.
        if (!dartExecutiveStatusRepository.findByCorpCode(corpCode).isEmpty()){
            throw new DartApiException("이미 존재하는 데이터입니다. 다시 요청할 수 없습니다.");
        }

        // 1. 반환받을 정확한 제네릭 타입을 정의합니다.
        ParameterizedTypeReference<DartApiResponseDto<DartExecutiveStatusItem>> responseType =
                new ParameterizedTypeReference<>() {};

        DartApiResponseDto<DartExecutiveStatusItem> responseDto;
        try {
            // 2. API 호출 시 body()에 정의한 타입을 넘겨줍니다.
            responseDto = client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/exctvSttus.json")
                            .queryParam("crtfc_key", dartApiKey)
                            .queryParam("corp_code", corpCode)
                            .queryParam("bsns_year", bsnsYear)
                            .queryParam("reprt_code", reprtCode)
                            .build())
                    .retrieve()
                    .body(responseType); // ⬅️ ParameterizedTypeReference 사용
        } catch (Exception e) {
            throw new DartApiException("DART API 호출 중 에러 발생",e);
        }
        log.info(Objects.requireNonNull(responseDto).toString());
        // 3. 응답 DTO의 상태를 확인합니다.
        if (responseDto == null || !"000".equals(responseDto.getStatus())) {
            throw new DartApiException("DART API 에러: status="
                    +(responseDto != null ? responseDto.getStatus() : "null")
                    +", message="
                    +(responseDto != null ? responseDto.getMessage() : "null response"));
        }

        // 4. DTO에서 직접 'list'를 가져옵니다.
        List<DartExecutiveStatusItem> items = responseDto.getList();
        if (items == null || items.isEmpty()) {
            log.info("DART API로부터 수신한 데이터가 없습니다.");
            return Collections.emptyList();
        }

        List<DartExecutiveStatus> entitiesToSave = new ArrayList<>();
        for (DartExecutiveStatusItem item : items) {

            // 5. API DTO를 DB Entity로 변환합니다.
            DartExecutiveStatus executive = DartExecutiveStatus.builder()
                    .rceptNo(item.getRceptNo())
                    .corpCls(item.getCorpCls())
                    .corpCode(item.getCorpCode())
                    .corpName(item.getCorpName())
                    .nm(item.getNm())
                    .sexdstn(item.getSexdstn())
                    .birthYm(item.getBirthYm())
                    .ofcps(item.getOfcps())
                    .rgistExctvAt(item.getRgistExctvAt())
                    .fteAt(item.getFteAt())
                    .chrgJob(item.getChrgJob())
                    .mainCareer(item.getMainCareer())
                    .mxmmShrhldrRelate(item.getMxmmShrhldrRelate())
                    .hffcPd(item.getHffcPd())
                    // DTO의 String 타입을 Entity의 LocalDate 타입으로 변환합니다.
                    // 날짜 형식이 'yyyy-MM-dd'가 아닌 경우, safeParseLocalDate에 두 번째 인자로 "yyyyMMdd" 등 패턴을 지정해야 합니다.
                    .tenureEndOn(support.safeParseLocalDate(item.getTenureEndOn()))
                    .stlmDt(support.safeParseLocalDate(item.getStlmDt()))
                    .build();
            entitiesToSave.add(executive);
        }

        // 6. 변환된 Entity를 DB에 저장하고, 저장된 결과를 받습니다.
        if (!entitiesToSave.isEmpty()) {
            List<DartExecutiveStatus> savedEntities = dartExecutiveStatusRepository.saveAll(entitiesToSave);
            // 7. 저장된 Entity 리스트를 Response DTO 리스트로 변환하여 반환합니다.
            return savedEntities.stream()
                    .map(DartExecutiveStatusResponse::from)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }


}
