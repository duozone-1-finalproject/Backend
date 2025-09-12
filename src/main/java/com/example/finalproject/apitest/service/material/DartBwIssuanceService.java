package com.example.finalproject.apitest.service.material;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.material.external.DartBwIssuanceItem;
import com.example.finalproject.apitest.dto.material.response.DartBwIssuanceResponse;
import com.example.finalproject.apitest.entity.material.DartBwIssuance;
import com.example.finalproject.apitest.repository.material.DartBwIssuanceRepository;
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
public class DartBwIssuanceService {

    private final DartApiCaller dartApiCaller;
    private final DartBwIssuanceRepository repository;
    private final Support support;

    @Transactional
    public List<DartBwIssuanceResponse> dartBwIssuanceCall(String corpCode, String bgnDe, String endDe) throws IOException {
        List<DartBwIssuance> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 신주인수권부사채권 발행결정 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartBwIssuanceResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartBwIssuanceItem>>() {};
        List<DartBwIssuanceItem> items = dartApiCaller.call(
                builder -> builder.path("/bdwtIsDecsn.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bgn_de", bgnDe)
                        .queryParam("end_de", endDe),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartBwIssuance> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartBwIssuance> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartBwIssuanceResponse::from)
                .collect(Collectors.toList());
    }

    private DartBwIssuance mapItemToEntity(DartBwIssuanceItem item) {
        DartBwIssuance entity = new DartBwIssuance();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setBdTm(item.getBdTm());
        entity.setBdKnd(item.getBdKnd());
        entity.setBdFta(support.safeParseLong(item.getBdFta()));
        entity.setAtcscRmismt(support.safeParseLong(item.getAtcscRmismt()));
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
        entity.setBdIntrEx(item.getBdIntrEx());
        entity.setBdIntrSf(item.getBdIntrSf());
        entity.setBdMtd(support.safeParseLocalDate(item.getBdMtd()));
        entity.setBdlisMthn(item.getBdlisMthn());
        entity.setExRt(item.getExRt());
        entity.setExPrc(support.safeParseLong(item.getExPrc()));
        entity.setExPrcDmth(item.getExPrcDmth());
        entity.setBdwtDivAtn(item.getBdwtDivAtn());
        entity.setNstkPymMth(item.getNstkPymMth());
        entity.setNstkIsstkKnd(item.getNstkIsstkKnd());
        entity.setNstkIsstkCnt(support.safeParseLong(item.getNstkIsstkCnt()));
        entity.setNstkIsstkIsstkVs(item.getNstkIsstkIsstkVs());
        entity.setExpdBgd(support.safeParseLocalDate(item.getExpdBgd()));
        entity.setExpdEdd(support.safeParseLocalDate(item.getExpdEdd()));
        entity.setActMktprcflCvprcLwtrsprc(support.safeParseLong(item.getActMktprcflCvprcLwtrsprc()));
        entity.setActMktprcflCvprcLwtrsprcBs(item.getActMktprcflCvprcLwtrsprcBs());
        entity.setRmislmtLt70p(support.safeParseLong(item.getRmislmtLt70p()));
        entity.setAbmg(item.getAbmg());
        entity.setSbd(support.safeParseLocalDate(item.getSbd()));
        entity.setPymd(support.safeParseLocalDate(item.getPymd()));
        entity.setRpcmcp(item.getRpcmcp());
        entity.setGrint(item.getGrint());
        entity.setBddd(support.safeParseLocalDate(item.getBddd()));
        entity.setOdAAtT(support.safeParseLong(item.getOdAAtT()));
        entity.setOdAAtB(support.safeParseLong(item.getOdAAtB()));
        entity.setAdtAAtn(item.getAdtAAtn());
        entity.setRsSmAtn(item.getRsSmAtn());
        entity.setExSmrR(item.getExSmrR());
        entity.setOvisLtdtl(item.getOvisLtdtl());
        entity.setFtcSttAtn(item.getFtcSttAtn());
        return entity;
    }
}
