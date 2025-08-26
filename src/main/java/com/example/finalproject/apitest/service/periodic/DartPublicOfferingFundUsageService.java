package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartPublicOfferingFundUsageItem;
import com.example.finalproject.apitest.dto.periodic.response.DartPublicOfferingFundUsageResponse;
import com.example.finalproject.apitest.entity.periodic.DartPublicOfferingFundUsage;
import com.example.finalproject.apitest.repository.periodic.DartPublicOfferingFundUsageRepository;
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
public class DartPublicOfferingFundUsageService {

    private final DartApiCaller dartApiCaller;
    private final DartPublicOfferingFundUsageRepository repository;
    private final Support support;

    @Transactional
    public List<DartPublicOfferingFundUsageResponse> dartPublicOfferingFundUsageCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartPublicOfferingFundUsage> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 공모자금 사용내역 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartPublicOfferingFundUsageResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartPublicOfferingFundUsageItem>>() {};
        List<DartPublicOfferingFundUsageItem> items = dartApiCaller.call(
                builder -> builder.path("/pssrpCptalUseDtls.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartPublicOfferingFundUsage> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartPublicOfferingFundUsage> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartPublicOfferingFundUsageResponse::from)
                .collect(Collectors.toList());
    }

    private DartPublicOfferingFundUsage mapItemToEntity(DartPublicOfferingFundUsageItem item) {
        DartPublicOfferingFundUsage entity = new DartPublicOfferingFundUsage();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setSeNm(item.getSeNm());
        entity.setTm(item.getTm());
        entity.setPayDe(support.safeParseLocalDate(item.getPayDe(), "yyyy.MM.dd"));
        entity.setPayAmount(support.safeParseLong(item.getPayAmount()));
        entity.setOnDclrtCptalUsePlan(item.getOnDclrtCptalUsePlan());
        entity.setRealCptalUseSttus(item.getRealCptalUseSttus());
        entity.setRsCptalUsePlanUseprps(item.getRsCptalUsePlanUseprps());
        entity.setRsCptalUsePlanPicureAmount(support.safeParseLong(item.getRsCptalUsePlanPicureAmount()));
        entity.setRealCptalUseDtlsCn(item.getRealCptalUseDtlsCn());
        entity.setRealCptalUseDtlsAmount(support.safeParseLong(item.getRealCptalUseDtlsAmount()));
        entity.setDffrncOccrrncResn(item.getDffrncOccrrncResn());
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
