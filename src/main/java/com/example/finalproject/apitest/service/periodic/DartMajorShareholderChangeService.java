package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartMajorShareholderChangeItem;
import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderChangeResponse;
import com.example.finalproject.apitest.entity.periodic.DartMajorShareholderChange;
import com.example.finalproject.apitest.repository.periodic.DartMajorShareholderChangeRepository;
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
public class DartMajorShareholderChangeService {

    private final DartApiCaller dartApiCaller;
    private final DartMajorShareholderChangeRepository repository;
    private final Support support;

    @Transactional
    public List<DartMajorShareholderChangeResponse> dartMajorShareholderChangeCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartMajorShareholderChange> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            // [수정] 데이터가 존재하면, API를 호출하지 않고 기존 데이터를 DTO로 변환하여 즉시 반환합니다.
            log.info("corpCode {}에 대한 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartMajorShareholderChangeResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartMajorShareholderChangeItem>>() {};

        List<DartMajorShareholderChangeItem> items = dartApiCaller.call(
                builder -> builder.path("/hyslrChgSttus.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) return Collections.emptyList();

        List<DartMajorShareholderChange> entitiesToSave = new ArrayList<>();
        for (DartMajorShareholderChangeItem item : items) {
            DartMajorShareholderChange entity = DartMajorShareholderChange.builder()
                    .rceptNo(item.getRceptNo())
                    .corpCls(item.getCorpCls())
                    .corpCode(item.getCorpCode())
                    .corpName(item.getCorpName())
                    .changeOn(support.safeParseLocalDate(item.getChangeOn(), "yyyy.MM.dd"))
                    .mxmmShrhldrNm(item.getMxmmShrhldrNm())
                    .posesnStockCo(support.safeParseLong(item.getPosesnStockCo()))
                    .qotaRt(support.safeParseDouble(item.getQotaRt()))
                    .changeCause(item.getChangeCause())
                    .rm(item.getRm())
                    .stlmDt(support.safeParseLocalDate(item.getStlmDt()))
                    .build();
            entitiesToSave.add(entity);
        }

        List<DartMajorShareholderChange> savedEntities = repository.saveAll(entitiesToSave);
        return savedEntities.stream()
                .map(DartMajorShareholderChangeResponse::from)
                .collect(Collectors.toList());
    }
}