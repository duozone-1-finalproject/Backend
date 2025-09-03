package com.example.finalproject.apitest.service.overview;


import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.overview.external.DartCompanyOverviewItem;
import com.example.finalproject.apitest.dto.overview.response.DartCompanyOverviewResponse;
import com.example.finalproject.apitest.dto.periodic.external.DartAuditServiceContractItem;
import com.example.finalproject.apitest.entity.overview.DartCompanyOverview;
import com.example.finalproject.apitest.repository.overview.DartCompanyOverviewRepository;
import com.example.finalproject.apitest.service.common.DartApiCaller;
import com.example.finalproject.apitest.service.common.Support;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DartCompanyOverviewService {

    private final DartApiCaller dartApiCaller;
    private final DartCompanyOverviewRepository repository;
    private final Support support;

    @Transactional
    public DartCompanyOverviewResponse dartCompanyOverviewCall(String corpCode) throws IOException {
        Optional<DartCompanyOverview> existingData = repository.findById(corpCode);
        if (existingData.isPresent()) {
            log.info("corpCode {}에 대한 기업개황 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return DartCompanyOverviewResponse.from(existingData.get());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartCompanyOverviewItem>>() {};
        // 이 API는 응답이 리스트가 아닌 단일 객체입니다.
        DartCompanyOverviewItem item = dartApiCaller.callSingle(
                builder -> builder.path("/company.json")
                        .queryParam("corp_code", corpCode),
                responseType
        );

        if (item == null) {
            return null;
        }

        DartCompanyOverview entityToSave = mapItemToEntity(item);
        DartCompanyOverview savedEntity = repository.save(entityToSave);

        return DartCompanyOverviewResponse.from(savedEntity);
    }

    private DartCompanyOverview mapItemToEntity(DartCompanyOverviewItem item) {
        DartCompanyOverview entity = new DartCompanyOverview();
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setCorpNameEng(item.getCorpNameEng());
        entity.setStockName(item.getStockName());
        entity.setStockCode(item.getStockCode());
        entity.setCeoNm(item.getCeoNm());
        entity.setCorpCls(item.getCorpCls());
        entity.setJurirNo(item.getJurirNo());
        entity.setBizrNo(item.getBizrNo());
        entity.setAdres(item.getAdres());
        entity.setHmUrl(item.getHmUrl());
        entity.setIrUrl(item.getIrUrl());
        entity.setPhnNo(item.getPhnNo());
        entity.setFaxNo(item.getFaxNo());
        entity.setIndutyCode(item.getIndutyCode());
        entity.setEstDt(support.safeParseLocalDate(item.getEstDt(), "yyyyMMdd"));
        entity.setAccMt(item.getAccMt());
        return entity;
    }
}
