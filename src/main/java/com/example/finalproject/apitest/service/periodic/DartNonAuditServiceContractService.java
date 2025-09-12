package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartNonAuditServiceContractItem;
import com.example.finalproject.apitest.dto.periodic.response.DartNonAuditServiceContractResponse;
import com.example.finalproject.apitest.entity.periodic.DartNonAuditServiceContract;
import com.example.finalproject.apitest.repository.periodic.DartNonAuditServiceContractRepository;
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
public class DartNonAuditServiceContractService {

    private final DartApiCaller dartApiCaller;
    private final DartNonAuditServiceContractRepository repository;
    private final Support support;

    @Transactional
    public List<DartNonAuditServiceContractResponse> dartNonAuditServiceContractCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartNonAuditServiceContract> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 비감사용역 계약체결 현황 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartNonAuditServiceContractResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartNonAuditServiceContractItem>>() {};
        List<DartNonAuditServiceContractItem> items = dartApiCaller.call(
                builder -> builder.path("/accnutAdtorNonAdtServcCnclsSttus.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartNonAuditServiceContract> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartNonAuditServiceContract> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartNonAuditServiceContractResponse::from)
                .collect(Collectors.toList());
    }

    private DartNonAuditServiceContract mapItemToEntity(DartNonAuditServiceContractItem item) {
        DartNonAuditServiceContract entity = new DartNonAuditServiceContract();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setBsnsYear(item.getBsnsYear());
        entity.setCntrctCnclsDe(support.safeParseLocalDate(item.getCntrctCnclsDe()));
        entity.setServcCn(item.getServcCn());
        entity.setServcExcPd(item.getServcExcPd());
        entity.setServcMendng(item.getServcMendng());
        entity.setRm(item.getRm());
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
