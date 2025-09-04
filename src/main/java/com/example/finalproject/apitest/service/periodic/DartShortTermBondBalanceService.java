package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartShortTermBondBalanceItem;
import com.example.finalproject.apitest.dto.periodic.response.DartShortTermBondBalanceResponse;
import com.example.finalproject.apitest.entity.periodic.DartShortTermBondBalance;
import com.example.finalproject.apitest.repository.periodic.DartShortTermBondBalanceRepository;
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
public class DartShortTermBondBalanceService {

    private final DartApiCaller dartApiCaller;
    private final DartShortTermBondBalanceRepository repository;
    private final Support support;

    @Transactional
    public List<DartShortTermBondBalanceResponse> dartShortTermBondBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartShortTermBondBalance> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 단기사채 미상환 잔액 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartShortTermBondBalanceResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartShortTermBondBalanceItem>>() {};
        List<DartShortTermBondBalanceItem> items = dartApiCaller.call(
                builder -> builder.path("/srtpdPsndbtNrdmpBlce.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartShortTermBondBalance> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartShortTermBondBalance> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartShortTermBondBalanceResponse::from)
                .collect(Collectors.toList());
    }

    private DartShortTermBondBalance mapItemToEntity(DartShortTermBondBalanceItem item) {
        DartShortTermBondBalance entity = new DartShortTermBondBalance();
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
        entity.setSm(support.safeParseLong(item.getSm()));
        entity.setIsuLmt(support.safeParseLong(item.getIsuLmt()));
        entity.setRemndrLmt(support.safeParseLong(item.getRemndrLmt()));
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
