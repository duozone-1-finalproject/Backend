package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartAuditOpinionItem;
import com.example.finalproject.apitest.dto.periodic.response.DartAuditOpinionResponse;
import com.example.finalproject.apitest.entity.periodic.DartAuditOpinion;
import com.example.finalproject.apitest.repository.periodic.DartAuditOpinionRepository;
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
public class DartAuditOpinionService {

    private final DartApiCaller dartApiCaller;
    private final DartAuditOpinionRepository repository;
    private final Support support;

    @Transactional
    public List<DartAuditOpinionResponse> dartAuditOpinionCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartAuditOpinion> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 회계감사인 의견 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartAuditOpinionResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartAuditOpinionItem>>() {};
        List<DartAuditOpinionItem> items = dartApiCaller.call(
                builder -> builder.path("/accnutAdtorNmNdAdtOpinion.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartAuditOpinion> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartAuditOpinion> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartAuditOpinionResponse::from)
                .collect(Collectors.toList());
    }

    private DartAuditOpinion mapItemToEntity(DartAuditOpinionItem item) {
        DartAuditOpinion entity = new DartAuditOpinion();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setBsnsYear(item.getBsnsYear());
        entity.setAdtor(item.getAdtor());
        entity.setAdtOpinion(item.getAdtOpinion());
        entity.setAdtReprtSpcmntMatter(item.getAdtReprtSpcmntMatter());
        entity.setEmphsMatter(item.getEmphsMatter());
        entity.setCoreAdtMatter(item.getCoreAdtMatter());
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
