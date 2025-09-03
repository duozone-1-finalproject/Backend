package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartUnregisteredExecutiveCompensationItem;
import com.example.finalproject.apitest.dto.periodic.response.DartUnregisteredExecutiveCompensationResponse;
import com.example.finalproject.apitest.entity.periodic.DartUnregisteredExecutiveCompensation;
import com.example.finalproject.apitest.repository.periodic.DartUnregisteredExecutiveCompensationRepository;
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
public class DartUnregisteredExecutiveCompensationService {

    private final DartApiCaller dartApiCaller;
    private final DartUnregisteredExecutiveCompensationRepository repository;
    private final Support support;

    @Transactional
    public List<DartUnregisteredExecutiveCompensationResponse> dartUnregisteredExecutiveCompensationCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartUnregisteredExecutiveCompensation> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 미등기임원 보수현황 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartUnregisteredExecutiveCompensationResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartUnregisteredExecutiveCompensationItem>>() {};
        List<DartUnregisteredExecutiveCompensationItem> items = dartApiCaller.call(
                builder -> builder.path("/unrstExctvMendngSttus.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartUnregisteredExecutiveCompensation> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartUnregisteredExecutiveCompensation> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartUnregisteredExecutiveCompensationResponse::from)
                .collect(Collectors.toList());
    }

    private DartUnregisteredExecutiveCompensation mapItemToEntity(DartUnregisteredExecutiveCompensationItem item) {
        DartUnregisteredExecutiveCompensation entity = new DartUnregisteredExecutiveCompensation();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setSe(item.getSe());
        entity.setNmpr(support.safeParseLong(item.getNmpr()));
        entity.setFyerSalaryTotamt(support.safeParseLong(item.getFyerSalaryTotamt()));
        entity.setJanSalaryAm(support.safeParseLong(item.getJanSalaryAm()));
        entity.setRm(item.getRm());
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
