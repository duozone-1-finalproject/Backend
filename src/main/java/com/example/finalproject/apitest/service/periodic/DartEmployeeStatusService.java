package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartEmployeeStatusItem;
import com.example.finalproject.apitest.dto.periodic.response.DartEmployeeStatusResponse;
import com.example.finalproject.apitest.entity.periodic.DartEmployeeStatus;
import com.example.finalproject.apitest.repository.periodic.DartEmployeeStatusRepository;
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
public class DartEmployeeStatusService {

    private final DartApiCaller dartApiCaller;
    private final DartEmployeeStatusRepository repository;
    private final Support support;

    @Transactional
    public List<DartEmployeeStatusResponse> dartEmployeeStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartEmployeeStatus> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 직원 현황 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartEmployeeStatusResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartEmployeeStatusItem>>() {};
        List<DartEmployeeStatusItem> items = dartApiCaller.call(
                builder -> builder.path("/empSttus.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartEmployeeStatus> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartEmployeeStatus> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartEmployeeStatusResponse::from)
                .collect(Collectors.toList());
    }

    private DartEmployeeStatus mapItemToEntity(DartEmployeeStatusItem item) {
        DartEmployeeStatus entity = new DartEmployeeStatus();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setFoBbm(item.getFoBbm());
        entity.setSexdstn(item.getSexdstn());
        entity.setReformBfeEmpCoRgllb(support.safeParseLong(item.getReformBfeEmpCoRgllb()));
        entity.setReformBfeEmpCoCnttk(support.safeParseLong(item.getReformBfeEmpCoCnttk()));
        entity.setReformBfeEmpCoEtc(support.safeParseLong(item.getReformBfeEmpCoEtc()));
        entity.setRgllbrCo(support.safeParseLong(item.getRgllbrCo()));
        entity.setRgllbrAbacptLabrrCo(support.safeParseLong(item.getRgllbrAbacptLabrrCo()));
        entity.setCnttkCo(support.safeParseLong(item.getCnttkCo()));
        entity.setCnttkAbacptLabrrCo(support.safeParseLong(item.getCnttkAbacptLabrrCo()));
        entity.setSm(support.safeParseLong(item.getSm()));
        entity.setAvrgCnwkSdytrn(item.getAvrgCnwkSdytrn());
        entity.setFyerSalaryTotamt(support.safeParseLong(item.getFyerSalaryTotamt()));
        entity.setJanSalaryAm(support.safeParseLong(item.getJanSalaryAm()));
        entity.setRm(item.getRm());
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
