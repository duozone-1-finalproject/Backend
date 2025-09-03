package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartTotalStockStatusItem;
import com.example.finalproject.apitest.dto.periodic.response.DartTotalStockStatusResponse;
import com.example.finalproject.apitest.entity.periodic.DartTotalStockStatus;
import com.example.finalproject.apitest.repository.periodic.DartTotalStockStatusRepository;
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
public class DartTotalStockStatusService {

    private final DartApiCaller dartApiCaller;
    private final DartTotalStockStatusRepository repository;
    private final Support support;

    @Transactional
    public List<DartTotalStockStatusResponse> dartTotalStockStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartTotalStockStatus> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 주식 총수 현황 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartTotalStockStatusResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartTotalStockStatusItem>>() {};
        List<DartTotalStockStatusItem> items = dartApiCaller.call(
                builder -> builder.path("/stockTotqySttus.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartTotalStockStatus> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartTotalStockStatus> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartTotalStockStatusResponse::from)
                .collect(Collectors.toList());
    }

    private DartTotalStockStatus mapItemToEntity(DartTotalStockStatusItem item) {
        DartTotalStockStatus entity = new DartTotalStockStatus();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setSe(item.getSe());
        entity.setIsuStockTotqy(support.safeParseLong(item.getIsuStockTotqy()));
        entity.setNowToIsuStockTotqy(support.safeParseLong(item.getNowToIsuStockTotqy()));
        entity.setNowToDcrsStockTotqy(support.safeParseLong(item.getNowToDcrsStockTotqy()));
        entity.setRedc(support.safeParseLong(item.getRedc()));
        entity.setProfitIncnr(support.safeParseLong(item.getProfitIncnr()));
        entity.setRdmstkRepy(support.safeParseLong(item.getRdmstkRepy()));
        entity.setEtc(support.safeParseLong(item.getEtc()));
        entity.setIstcTotqy(support.safeParseLong(item.getIstcTotqy()));
        entity.setTesstkCo(support.safeParseLong(item.getTesstkCo()));
        entity.setDistbStockCo(support.safeParseLong(item.getDistbStockCo()));
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
