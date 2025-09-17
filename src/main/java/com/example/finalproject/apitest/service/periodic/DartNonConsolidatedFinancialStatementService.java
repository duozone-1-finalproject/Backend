package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartNonConsolidatedFinancialStatementItem;
import com.example.finalproject.apitest.dto.periodic.response.DartNonConsolidatedFinancialStatementResponse;
import com.example.finalproject.apitest.entity.periodic.DartNonConsolidatedFinancialStatement;
import com.example.finalproject.apitest.repository.periodic.DartNonConsolidatedFinancialStatementRepository;
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
public class DartNonConsolidatedFinancialStatementService {

    private final DartApiCaller dartApiCaller;
    private final DartNonConsolidatedFinancialStatementRepository repository;
    private final Support support;

    @Transactional
    public List<DartNonConsolidatedFinancialStatementResponse> dartNonConsolidatedFinancialStatementCall(String corpCode, String bsnsYear, String reprtCode, String fsDiv) throws IOException {
        List<DartNonConsolidatedFinancialStatement> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 단일회사 전체 재무제표 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartNonConsolidatedFinancialStatementResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartNonConsolidatedFinancialStatementItem>>() {};
        List<DartNonConsolidatedFinancialStatementItem> items = dartApiCaller.call(
                builder -> builder.path("/fnlttSinglAcntAll.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode)
                        .queryParam("fs_div", fsDiv),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartNonConsolidatedFinancialStatement> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartNonConsolidatedFinancialStatement> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartNonConsolidatedFinancialStatementResponse::from)
                .collect(Collectors.toList());
    }

    private DartNonConsolidatedFinancialStatement mapItemToEntity(DartNonConsolidatedFinancialStatementItem item) {
        DartNonConsolidatedFinancialStatement entity = new DartNonConsolidatedFinancialStatement();
        entity.setRceptNo(item.getRceptNo());
        entity.setReprtCode(item.getReprtCode());
        entity.setBsnsYear(item.getBsnsYear());
        entity.setCorpCode(item.getCorpCode());
        entity.setSjDiv(item.getSjDiv());
        entity.setSjNm(item.getSjNm());
        entity.setAccountId(item.getAccountId());
        entity.setAccountNm(item.getAccountNm());
        entity.setAccountDetail(item.getAccountDetail());
        entity.setThstrmNm(item.getThstrmNm());
        entity.setThstrmAmount(support.safeParseLong(item.getThstrmAmount()));
        entity.setThstrmAddAmount(support.safeParseLong(item.getThstrmAddAmount()));
        entity.setFrmtrmNm(item.getFrmtrmNm());
        entity.setFrmtrmAmount(support.safeParseLong(item.getFrmtrmAmount()));
        entity.setFrmtrmQNm(item.getFrmtrmQNm());
        entity.setFrmtrmQAmount(support.safeParseLong(item.getFrmtrmQAmount()));
        entity.setFrmtrmAddAmount(support.safeParseLong(item.getFrmtrmAddAmount()));
        entity.setBfefrmtrmNm(item.getBfefrmtrmNm());
        entity.setBfefrmtrmAmount(support.safeParseLong(item.getBfefrmtrmAmount()));
        entity.setOrd(support.safeParseInteger(item.getOrd()));
        entity.setCurrency(item.getCurrency());
        return entity;
    }
}
