package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartTreasuryStockStatusItem;
import com.example.finalproject.apitest.dto.periodic.response.DartTreasuryStockStatusResponse;
import com.example.finalproject.apitest.entity.periodic.DartTreasuryStockStatus;
import com.example.finalproject.apitest.repository.periodic.DartTreasuryStockStatusRepository;
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
public class DartTreasuryStockStatusService {

    private final DartApiCaller dartApiCaller;
    private final DartTreasuryStockStatusRepository repository;
    private final Support support;

    @Transactional
    public List<DartTreasuryStockStatusResponse> dartTreasuryStockStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartTreasuryStockStatus> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 자기주식 취득/처분 현황 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartTreasuryStockStatusResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartTreasuryStockStatusItem>>() {};
        List<DartTreasuryStockStatusItem> items = dartApiCaller.call(
                builder -> builder.path("/tesstkAcqsDspsSttus.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartTreasuryStockStatus> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartTreasuryStockStatus> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartTreasuryStockStatusResponse::from)
                .collect(Collectors.toList());
    }

    private DartTreasuryStockStatus mapItemToEntity(DartTreasuryStockStatusItem item) {
        DartTreasuryStockStatus entity = new DartTreasuryStockStatus();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setAcqsMth1(item.getAcqsMth1());
        entity.setAcqsMth2(item.getAcqsMth2());
        entity.setAcqsMth3(item.getAcqsMth3());
        entity.setStockKnd(item.getStockKnd());
        entity.setBsisQy(support.safeParseLong(item.getBsisQy()));
        entity.setChangeQyAcqs(support.safeParseLong(item.getChangeQyAcqs()));
        entity.setChangeQyDsps(support.safeParseLong(item.getChangeQyDsps()));
        entity.setChangeQyIncnr(support.safeParseLong(item.getChangeQyIncnr()));
        entity.setTrmendQy(support.safeParseLong(item.getTrmendQy()));
        entity.setRm(item.getRm());
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
