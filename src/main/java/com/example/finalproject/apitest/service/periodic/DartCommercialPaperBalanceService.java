package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartCommercialPaperBalanceItem;
import com.example.finalproject.apitest.dto.periodic.response.DartCommercialPaperBalanceResponse;
import com.example.finalproject.apitest.entity.periodic.DartCommercialPaperBalance;
import com.example.finalproject.apitest.repository.periodic.DartCommercialPaperBalanceRepository;
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
public class DartCommercialPaperBalanceService {

    private final DartApiCaller dartApiCaller;
    private final DartCommercialPaperBalanceRepository repository;
    private final Support support;

    @Transactional
    public List<DartCommercialPaperBalanceResponse> dartCommercialPaperBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartCommercialPaperBalance> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 기업어음증권 미상환 잔액 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartCommercialPaperBalanceResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartCommercialPaperBalanceItem>>() {};
        List<DartCommercialPaperBalanceItem> items = dartApiCaller.call(
                builder -> builder.path("/entrprsBilScritsNrdmpBlce.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartCommercialPaperBalance> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartCommercialPaperBalance> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartCommercialPaperBalanceResponse::from)
                .collect(Collectors.toList());
    }

    private DartCommercialPaperBalance mapItemToEntity(DartCommercialPaperBalanceItem item) {
        DartCommercialPaperBalance entity = new DartCommercialPaperBalance();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setRemndrExprtn1(item.getRemndrExprtn1());
        entity.setRemndrExprtn2(item.getRemndrExprtn2());
        entity.setDe10Below(support.safeParseLong(item.getDe10Below()));
        entity.setDe10ExcessDe30Below(support.safeParseLong(item.getDe10ExcessDe30Below()));
        entity.setDe30ExcessDe90Below(support.safeParseLong(item.getDe30ExcessDe90Below()));
        entity.setDe90ExcessDe180Below(support.safeParseLong(item.getDe90ExcessDe180Below()));
        entity.setDe180ExcessYy1Below(support.safeParseLong(item.getDe180ExcessYy1Below()));
        entity.setYy1ExcessYy2Below(support.safeParseLong(item.getYy1ExcessYy2Below()));
        entity.setYy2ExcessYy3Below(support.safeParseLong(item.getYy2ExcessYy3Below()));
        entity.setYy3Excess(support.safeParseLong(item.getYy3Excess()));
        entity.setSm(support.safeParseLong(item.getSm()));
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
