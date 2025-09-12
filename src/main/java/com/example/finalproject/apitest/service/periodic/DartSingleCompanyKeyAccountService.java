package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartSingleCompanyKeyAccountItem;
import com.example.finalproject.apitest.dto.periodic.response.DartSingleCompanyKeyAccountResponse;
import com.example.finalproject.apitest.entity.periodic.DartSingleCompanyKeyAccount;
import com.example.finalproject.apitest.repository.periodic.DartSingleCompanyKeyAccountRepository;
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
public class DartSingleCompanyKeyAccountService {

    private final DartApiCaller dartApiCaller;
    private final DartSingleCompanyKeyAccountRepository repository;
    private final Support support;

    @Transactional
    public List<DartSingleCompanyKeyAccountResponse> dartSingleCompanyKeyAccountCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        // 이 API는 corpCode가 아닌 rceptNo 기반으로 데이터를 식별해야 할 수 있습니다.
        // 우선 corpCode로 기존 로직을 유지하되, 필요시 rceptNo 기반으로 변경해야 합니다.
        // List<DartSingleCompanyKeyAccount> existingData = repository.findByCorpCode(corpCode);
        // if (!existingData.isEmpty()) {
        //     log.info("corpCode {}에 대한 단일회사 주요계정 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
        //     return existingData.stream()
        //             .map(DartSingleCompanyKeyAccountResponse::from)
        //             .collect(Collectors.toList());
        // }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartSingleCompanyKeyAccountItem>>() {};
        List<DartSingleCompanyKeyAccountItem> items = dartApiCaller.call(
                builder -> builder.path("/fnlttSinglAcnt.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartSingleCompanyKeyAccount> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartSingleCompanyKeyAccount> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartSingleCompanyKeyAccountResponse::from)
                .collect(Collectors.toList());
    }

    private DartSingleCompanyKeyAccount mapItemToEntity(DartSingleCompanyKeyAccountItem item) {
        DartSingleCompanyKeyAccount entity = new DartSingleCompanyKeyAccount();
        entity.setRceptNo(item.getRceptNo());
        entity.setBsnsYear(item.getBsnsYear());
        entity.setStockCode(item.getStockCode());
        entity.setReprtCode(item.getReprtCode());
        entity.setAccountNm(item.getAccountNm());
        entity.setFsDiv(item.getFsDiv());
        entity.setFsNm(item.getFsNm());
        entity.setSjDiv(item.getSjDiv());
        entity.setSjNm(item.getSjNm());
        entity.setThstrmNm(item.getThstrmNm());
        entity.setThstrmDt(item.getThstrmDt());
        entity.setThstrmAmount(support.safeParseLong(item.getThstrmAmount()));
        entity.setThstrmAddAmount(support.safeParseLong(item.getThstrmAddAmount()));
        entity.setFrmtrmNm(item.getFrmtrmNm());
        entity.setFrmtrmDt(item.getFrmtrmDt());
        entity.setFrmtrmAmount(support.safeParseLong(item.getFrmtrmAmount()));
        entity.setFrmtrmAddAmount(support.safeParseLong(item.getFrmtrmAddAmount()));
        entity.setBfefrmtrmNm(item.getBfefrmtrmNm());
        entity.setBfefrmtrmDt(item.getBfefrmtrmDt());
        entity.setBfefrmtrmAmount(support.safeParseLong(item.getBfefrmtrmAmount()));
        entity.setOrd(support.safeParseInteger(item.getOrd()));
        entity.setCurrency(item.getCurrency());
        return entity;
    }
}
