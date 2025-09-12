package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartMajorShareholderStatusItem;
import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderStatusResponse;
import com.example.finalproject.apitest.entity.periodic.DartMajorShareholderStatus;
import com.example.finalproject.apitest.repository.periodic.DartMajorShareholderStatusRepository;
import com.example.finalproject.apitest.service.common.DartApiCaller;
import com.example.finalproject.apitest.service.common.Support;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DartMajorShareholderStatusService {

    private final DartApiCaller dartApiCaller;
    private final DartMajorShareholderStatusRepository repository;
    private final Support support;

    @Transactional
    public List<DartMajorShareholderStatusResponse> dartMajorShareholderStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartMajorShareholderStatus> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            // [수정] 데이터가 존재하면, API를 호출하지 않고 기존 데이터를 DTO로 변환하여 즉시 반환합니다.
            log.info("corpCode {}에 대한 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartMajorShareholderStatusResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartMajorShareholderStatusItem>>() {};

        List<DartMajorShareholderStatusItem> items = dartApiCaller.call(
                builder -> builder.path("/hyslrSttus.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) return Collections.emptyList();

        List<DartMajorShareholderStatus> entitiesToSave = new ArrayList<>();
        for (DartMajorShareholderStatusItem item : items) {
            if ("계".equals(item.getNm())) continue;

            DartMajorShareholderStatus entity = DartMajorShareholderStatus.builder()
                    .rceptNo(item.getRceptNo())
                    .corpCls(item.getCorpCls())
                    .corpCode(item.getCorpCode())
                    .corpName(item.getCorpName())
                    .nm(item.getNm())
                    .relate(item.getRelate())
                    .stockKnd(item.getStockKnd())
                    .bsisPosesnStockCo(support.safeParseLong(item.getBsisPosesnStockCo()))
                    .bsisPosesnStockQotaRt(support.safeParseDouble(item.getBsisPosesnStockQotaRt()))
                    .trmendPosesnStockCo(support.safeParseLong(item.getTrmendPosesnStockCo()))
                    .trmendPosesnStockQotaRt(support.safeParseDouble(item.getTrmendPosesnStockQotaRt()))
                    .rm(item.getRm())
                    .stlmDt(support.safeParseLocalDate(item.getStlmDt()))
                    .build();
            entitiesToSave.add(entity);
        }

        List<DartMajorShareholderStatus> savedEntities = repository.saveAll(entitiesToSave);
        return savedEntities.stream()
                .map(DartMajorShareholderStatusResponse::from)
                .collect(Collectors.toList());
    }
}
