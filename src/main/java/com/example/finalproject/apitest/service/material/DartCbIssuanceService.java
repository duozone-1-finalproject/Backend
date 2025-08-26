package com.example.finalproject.apitest.service.material;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.material.external.DartCbIssuanceItem;
import com.example.finalproject.apitest.dto.material.response.DartCbIssuanceResponse;
import com.example.finalproject.apitest.entity.material.DartCbIssuance;
import com.example.finalproject.apitest.repository.material.DartCbIssuanceRepository;
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
public class DartCbIssuanceService {

    private final DartApiCaller dartApiCaller;
    private final DartCbIssuanceRepository repository;
    private final Support support;

    @Transactional
    public List<DartCbIssuanceResponse> dartCbIssuanceCall(String corpCode, String bgnDe, String endDe) throws IOException {
        // 1. DB에서 데이터 조회
        List<DartCbIssuance> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 전환사채권 발행결정 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartCbIssuanceResponse::from)
                    .collect(Collectors.toList());
        }

        // 2. DB에 데이터가 없으면 DART API 호출
        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartCbIssuanceItem>>() {};
        List<DartCbIssuanceItem> items = dartApiCaller.call(
                builder -> builder.path("/cvbdIsDecsn.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bgn_de", bgnDe)
                        .queryParam("end_de", endDe),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. API 결과를 Entity로 변환
        List<DartCbIssuance> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        // 4. DB에 저장
        List<DartCbIssuance> savedEntities = repository.saveAll(entitiesToSave);

        // 5. 저장된 Entity를 Response DTO로 변환하여 반환
        return savedEntities.stream()
                .map(DartCbIssuanceResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * DartCbIssuanceItem DTO를 DartCbIssuance Entity로 변환합니다.
     * @param item API 응답 아이템
     * @return 변환된 Entity
     */
    private DartCbIssuance mapItemToEntity(DartCbIssuanceItem item) {
        DartCbIssuance entity = new DartCbIssuance();
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
        entity.setCvPrc(support.safeParseLong(item.getCvPrc()));
        entity.setCvPrcDmth(item.getCvPrcDmth());
        entity.setCvRt(item.getCvRt());
        entity.setCvRqpdBgd(support.safeParseLocalDate(item.getCvRqpdBgd()));
        entity.setCvRqpdEdd(support.safeParseLocalDate(item.getCvRqpdEdd()));
        entity.setCvIsstkKnd(item.getCvIsstkKnd());
        entity.setCvIsstkCnt(support.safeParseLong(item.getCvIsstkCnt()));
        entity.setCvIsstkIsstkVs(item.getCvIsstkIsstkVs());
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
