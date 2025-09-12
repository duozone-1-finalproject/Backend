package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartPrivatePlacementFundUsageItem;
import com.example.finalproject.apitest.dto.periodic.response.DartPrivatePlacementFundUsageResponse;
import com.example.finalproject.apitest.entity.periodic.DartPrivatePlacementFundUsage;
import com.example.finalproject.apitest.repository.periodic.DartPrivatePlacementFundUsageRepository;
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
public class DartPrivatePlacementFundUsageService {

    private final DartApiCaller dartApiCaller;
    private final DartPrivatePlacementFundUsageRepository repository;
    private final Support support;

    @Transactional
    public List<DartPrivatePlacementFundUsageResponse> dartPrivatePlacementFundUsageCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartPrivatePlacementFundUsage> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 사모자금 사용내역 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartPrivatePlacementFundUsageResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartPrivatePlacementFundUsageItem>>() {};
        List<DartPrivatePlacementFundUsageItem> items = dartApiCaller.call(
                builder -> builder.path("/prvsrpCptalUseDtls.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartPrivatePlacementFundUsage> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartPrivatePlacementFundUsage> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartPrivatePlacementFundUsageResponse::from)
                .collect(Collectors.toList());
    }

    private DartPrivatePlacementFundUsage mapItemToEntity(DartPrivatePlacementFundUsageItem item) {
        DartPrivatePlacementFundUsage entity = new DartPrivatePlacementFundUsage();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setSeNm(item.getSeNm());
        entity.setTm(item.getTm());
        entity.setPayDe(support.safeParseLocalDate(item.getPayDe(), "yyyy.MM.dd"));
        entity.setPayAmount(support.safeParseLong(item.getPayAmount()));
        entity.setCptalUsePlan(item.getCptalUsePlan());
        entity.setRealCptalUseSttus(item.getRealCptalUseSttus());
        entity.setMtrptCptalUsePlanUseprps(item.getMtrptCptalUsePlanUseprps());
        entity.setMtrptCptalUsePlanPicureAmount(support.safeParseLong(item.getMtrptCptalUsePlanPicureAmount()));
        entity.setRealCptalUseDtlsCn(item.getRealCptalUseDtlsCn());
        entity.setRealCptalUseDtlsAmount(support.safeParseLong(item.getRealCptalUseDtlsAmount()));
        entity.setDffrncOccrrncResn(item.getDffrncOccrrncResn());
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
