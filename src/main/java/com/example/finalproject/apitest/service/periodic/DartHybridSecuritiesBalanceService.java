package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartHybridSecuritiesBalanceItem;
import com.example.finalproject.apitest.dto.periodic.response.DartHybridSecuritiesBalanceResponse;
import com.example.finalproject.apitest.entity.periodic.DartHybridSecuritiesBalance;
import com.example.finalproject.apitest.repository.periodic.DartHybridSecuritiesBalanceRepository;
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
public class DartHybridSecuritiesBalanceService {

    private final DartApiCaller dartApiCaller;
    private final DartHybridSecuritiesBalanceRepository repository;
    private final Support support;

    @Transactional
    public List<DartHybridSecuritiesBalanceResponse> dartHybridSecuritiesBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartHybridSecuritiesBalance> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 신종자본증권 미상환 잔액 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartHybridSecuritiesBalanceResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartHybridSecuritiesBalanceItem>>() {};
        List<DartHybridSecuritiesBalanceItem> items = dartApiCaller.call(
                builder -> builder.path("/newCaplScritsNrdmpBlce.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartHybridSecuritiesBalance> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartHybridSecuritiesBalance> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartHybridSecuritiesBalanceResponse::from)
                .collect(Collectors.toList());
    }

    private DartHybridSecuritiesBalance mapItemToEntity(DartHybridSecuritiesBalanceItem item) {
        DartHybridSecuritiesBalance entity = new DartHybridSecuritiesBalance();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setRemndrExprtn1(item.getRemndrExprtn1());
        entity.setRemndrExprtn2(item.getRemndrExprtn2());
        entity.setYy1Below(support.safeParseLong(item.getYy1Below()));
        entity.setYy1ExcessYy5Below(support.safeParseLong(item.getYy1ExcessYy5Below()));
        entity.setYy5ExcessYy10Below(support.safeParseLong(item.getYy5ExcessYy10Below()));
        entity.setYy10ExcessYy15Below(support.safeParseLong(item.getYy10ExcessYy15Below()));
        entity.setYy15ExcessYy20Below(support.safeParseLong(item.getYy15ExcessYy20Below()));
        entity.setYy20ExcessYy30Below(support.safeParseLong(item.getYy20ExcessYy30Below()));
        entity.setYy30Excess(support.safeParseLong(item.getYy30Excess()));
        entity.setSm(support.safeParseLong(item.getSm()));
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
