package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartCorporateBondBalanceItem;
import com.example.finalproject.apitest.dto.periodic.response.DartCorporateBondBalanceResponse;
import com.example.finalproject.apitest.entity.periodic.DartCorporateBondBalance;
import com.example.finalproject.apitest.repository.periodic.DartCorporateBondBalanceRepository;
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
public class DartCorporateBondBalanceService {

    private final DartApiCaller dartApiCaller;
    private final DartCorporateBondBalanceRepository repository;
    private final Support support;

    @Transactional
    public List<DartCorporateBondBalanceResponse> dartCorporateBondBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartCorporateBondBalance> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 회사채 미상환 잔액 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartCorporateBondBalanceResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartCorporateBondBalanceItem>>() {};
        List<DartCorporateBondBalanceItem> items = dartApiCaller.call(
                builder -> builder.path("/cprndNrdmpBlce.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartCorporateBondBalance> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartCorporateBondBalance> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartCorporateBondBalanceResponse::from)
                .collect(Collectors.toList());
    }

    private DartCorporateBondBalance mapItemToEntity(DartCorporateBondBalanceItem item) {
        DartCorporateBondBalance entity = new DartCorporateBondBalance();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setRemndrExprtn1(item.getRemndrExprtn1());
        entity.setRemndrExprtn2(item.getRemndrExprtn2());
        entity.setYy1Below(support.safeParseLong(item.getYy1Below()));
        entity.setYy1ExcessYy2Below(support.safeParseLong(item.getYy1ExcessYy2Below()));
        entity.setYy2ExcessYy3Below(support.safeParseLong(item.getYy2ExcessYy3Below()));
        entity.setYy3ExcessYy4Below(support.safeParseLong(item.getYy3ExcessYy4Below()));
        entity.setYy4ExcessYy5Below(support.safeParseLong(item.getYy4ExcessYy5Below()));
        entity.setYy5ExcessYy10Below(support.safeParseLong(item.getYy5ExcessYy10Below()));
        entity.setYy10Excess(support.safeParseLong(item.getYy10Excess()));
        entity.setSm(support.safeParseLong(item.getSm()));
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
