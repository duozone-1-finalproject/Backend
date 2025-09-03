package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartOutsideDirectorChangeStatusItem;
import com.example.finalproject.apitest.dto.periodic.response.DartOutsideDirectorChangeStatusResponse;
import com.example.finalproject.apitest.entity.periodic.DartOutsideDirectorChangeStatus;
import com.example.finalproject.apitest.repository.periodic.DartOutsideDirectorChangeStatusRepository;
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
public class DartOutsideDirectorChangeStatusService {

    private final DartApiCaller dartApiCaller;
    private final DartOutsideDirectorChangeStatusRepository repository;
    private final Support support;

    @Transactional
    public List<DartOutsideDirectorChangeStatusResponse> dartOutsideDirectorChangeStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartOutsideDirectorChangeStatus> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 사외이사 변동현황 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartOutsideDirectorChangeStatusResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartOutsideDirectorChangeStatusItem>>() {};
        List<DartOutsideDirectorChangeStatusItem> items = dartApiCaller.call(
                builder -> builder.path("/outcmpnyDrctrNdChangeSttus.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartOutsideDirectorChangeStatus> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartOutsideDirectorChangeStatus> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartOutsideDirectorChangeStatusResponse::from)
                .collect(Collectors.toList());
    }

    private DartOutsideDirectorChangeStatus mapItemToEntity(DartOutsideDirectorChangeStatusItem item) {
        DartOutsideDirectorChangeStatus entity = new DartOutsideDirectorChangeStatus();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setDrctrCo(support.safeParseLong(item.getDrctrCo()));
        entity.setOtcmpDrctrCo(support.safeParseLong(item.getOtcmpDrctrCo()));
        entity.setApnt(support.safeParseLong(item.getApnt()));
        entity.setRlsofc(support.safeParseLong(item.getRlsofc()));
        entity.setMdstrmResig(support.safeParseLong(item.getMdstrmResig()));
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
