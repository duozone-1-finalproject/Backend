package com.example.finalproject.apitest.service.material;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.material.external.DartCocoBondIssuanceItem;
import com.example.finalproject.apitest.dto.material.response.DartCocoBondIssuanceResponse;
import com.example.finalproject.apitest.entity.material.DartCocoBondIssuance;
import com.example.finalproject.apitest.repository.material.DartCocoBondIssuanceRepository;
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
public class DartCocoBondIssuanceService {

    private final DartApiCaller dartApiCaller;
    private final DartCocoBondIssuanceRepository repository;
    private final Support support;

    @Transactional
    public List<DartCocoBondIssuanceResponse> dartCocoBondIssuanceCall(String corpCode, String bgnDe, String endDe) throws IOException {
        List<DartCocoBondIssuance> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 상각형 조건부자본증권 발행결정 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartCocoBondIssuanceResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartCocoBondIssuanceItem>>() {};
        List<DartCocoBondIssuanceItem> items = dartApiCaller.call(
                builder -> builder.path("/wdCocobdIsDecsn.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bgn_de", bgnDe)
                        .queryParam("end_de", endDe),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartCocoBondIssuance> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartCocoBondIssuance> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartCocoBondIssuanceResponse::from)
                .collect(Collectors.toList());
    }

    private DartCocoBondIssuance mapItemToEntity(DartCocoBondIssuanceItem item) {
        DartCocoBondIssuance entity = new DartCocoBondIssuance();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setBdTm(item.getBdTm());
        entity.setBdKnd(item.getBdKnd());
        entity.setBdFta(support.safeParseLong(item.getBdFta()));
        entity.setOvisFta(support.safeParseLong(item.getOvisFta()));
        entity.setOvisFtaCrn(item.getOvisFtaCrn());
        entity.setOvisSter(item.getOvisSter());
        entity.setOvisIsar(item.getOvisIsar());
        entity.setOvisMktnm(item.getOvisMktnm());
        entity.setFdppFclt(support.safeParseLong(item.getFdppFclt()));
        entity.setFdppBsninh(support.safeParseLong(item.getFdppBsninh()));
        entity.setFdppOp(support.safeParseLong(item.getFdppOp()));
        entity.setFdppDtrp(support.safeParseLong(item.getFdppDtrp()));
        entity.setFdppOcsa(support.safeParseLong(item.getFdppOcsa()));
        entity.setFdppEtc(support.safeParseLong(item.getFdppEtc()));
        entity.setBdItrSf(item.getBdItrSf());
        entity.setBdIntrEx(item.getBdIntrEx());
        entity.setBdMtd(support.safeParseLocalDate(item.getBdMtd()));
        entity.setDbtrsSc(item.getDbtrsSc());
        entity.setSbd(support.safeParseLocalDate(item.getSbd()));
        entity.setPymd(support.safeParseLocalDate(item.getPymd()));
        entity.setRpcmcp(item.getRpcmcp());
        entity.setGrint(item.getGrint());
        entity.setBddd(support.safeParseLocalDate(item.getBddd()));
        entity.setOdAAtB(support.safeParseLong(item.getOdAAtB()));
        entity.setOdAAtT(support.safeParseLong(item.getOdAAtT()));
        entity.setAdtAAtn(item.getAdtAAtn());
        entity.setRsSmAtn(item.getRsSmAtn());
        entity.setExSmrR(item.getExSmrR());
        entity.setOvisLtdtl(item.getOvisLtdtl());
        entity.setFtcAttAtn(item.getFtcAttAtn());
        return entity;
    }
}
