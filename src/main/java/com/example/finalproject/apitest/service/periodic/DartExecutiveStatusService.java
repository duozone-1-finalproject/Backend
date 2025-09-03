package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartExecutiveStatusItem;
import com.example.finalproject.apitest.dto.periodic.response.DartExecutiveStatusResponse;
import com.example.finalproject.apitest.entity.periodic.DartExecutiveStatus;
import com.example.finalproject.apitest.repository.periodic.DartExecutiveStatusRepository;
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
public class DartExecutiveStatusService {

    private final DartApiCaller dartApiCaller;
    private final DartExecutiveStatusRepository repository;
    private final Support support;

    @Transactional
    public List<DartExecutiveStatusResponse> dartExecutiveStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartExecutiveStatus> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            // [수정] 데이터가 존재하면, API를 호출하지 않고 기존 데이터를 DTO로 변환하여 즉시 반환합니다.
            log.info("corpCode {}에 대한 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartExecutiveStatusResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartExecutiveStatusItem>>() {};

        List<DartExecutiveStatusItem> items = dartApiCaller.call(
                builder -> builder.path("/exctvSttus.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) return Collections.emptyList();

        List<DartExecutiveStatus> entitiesToSave = new ArrayList<>();
        for (DartExecutiveStatusItem item : items) {
            DartExecutiveStatus entity = DartExecutiveStatus.builder()
                    .rceptNo(item.getRceptNo())
                    .corpCls(item.getCorpCls())
                    .corpCode(item.getCorpCode())
                    .corpName(item.getCorpName())
                    .nm(item.getNm())
                    .sexdstn(item.getSexdstn())
                    .birthYm(item.getBirthYm())
                    .ofcps(item.getOfcps())
                    .rgistExctvAt(item.getRgistExctvAt())
                    .fteAt(item.getFteAt())
                    .chrgJob(item.getChrgJob())
                    .mainCareer(item.getMainCareer())
                    .mxmmShrhldrRelate(item.getMxmmShrhldrRelate())
                    .hffcPd(item.getHffcPd())
                    .tenureEndOn(support.safeParseLocalDate(item.getTenureEndOn()))
                    .stlmDt(support.safeParseLocalDate(item.getStlmDt()))
                    .build();
            entitiesToSave.add(entity);
        }

        List<DartExecutiveStatus> savedEntities = repository.saveAll(entitiesToSave);
        return savedEntities.stream()
                .map(DartExecutiveStatusResponse::from)
                .collect(Collectors.toList());
    }
}
