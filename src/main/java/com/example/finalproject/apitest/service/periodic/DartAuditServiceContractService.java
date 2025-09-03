package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartAuditServiceContractItem;
import com.example.finalproject.apitest.dto.periodic.response.DartAuditServiceContractResponse;
import com.example.finalproject.apitest.entity.periodic.DartAuditServiceContract;
import com.example.finalproject.apitest.repository.periodic.DartAuditServiceContractRepository;
import com.example.finalproject.apitest.service.common.DartApiCaller;
import com.example.finalproject.apitest.service.common.Support;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DartAuditServiceContractService {

    private final DartApiCaller dartApiCaller;
    private final DartAuditServiceContractRepository repository;
    private final Support support;

    @Transactional
    public List<DartAuditServiceContractResponse> dartAuditServiceContractCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartAuditServiceContract> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 감사용역체결현황 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartAuditServiceContractResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartAuditServiceContractItem>>() {};
        List<DartAuditServiceContractItem> items = dartApiCaller.call(
                builder -> builder.path("/adtServcCnclsSttus.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartAuditServiceContract> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartAuditServiceContract> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartAuditServiceContractResponse::from)
                .collect(Collectors.toList());
    }

    private DartAuditServiceContract mapItemToEntity(DartAuditServiceContractItem item) {
        DartAuditServiceContract entity = new DartAuditServiceContract();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setBsnsYear(item.getBsnsYear());
        entity.setAdtor(item.getAdtor());
        entity.setCn(item.getCn());
        entity.setMendng(item.getMendng());
        entity.setTotReqreTime(item.getTotReqreTime());
        entity.setAdtCntrctDtlsMendng(item.getAdtCntrctDtlsMendng());
        entity.setAdtCntrctDtlsTime(item.getAdtCntrctDtlsTime());
        entity.setRealExcDtlsMendng(item.getRealExcDtlsMendng());
        entity.setRealExcDtlsTime(item.getRealExcDtlsTime());
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
