package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartCompensationApprovalItem;
import com.example.finalproject.apitest.dto.periodic.response.DartCompensationApprovalResponse;
import com.example.finalproject.apitest.entity.periodic.DartCompensationApproval;
import com.example.finalproject.apitest.repository.periodic.DartCompensationApprovalRepository;
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
public class DartCompensationApprovalService {

    private final DartApiCaller dartApiCaller;
    private final DartCompensationApprovalRepository repository;
    private final Support support;

    @Transactional
    public List<DartCompensationApprovalResponse> dartCompensationApprovalCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartCompensationApproval> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 이사/감사 보수현황 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartCompensationApprovalResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartCompensationApprovalItem>>() {};
        List<DartCompensationApprovalItem> items = dartApiCaller.call(
                builder -> builder.path("/drctrAdtAllMendngSttusGmtsckConfmAmount.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartCompensationApproval> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartCompensationApproval> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartCompensationApprovalResponse::from)
                .collect(Collectors.toList());
    }

    private DartCompensationApproval mapItemToEntity(DartCompensationApprovalItem item) {
        DartCompensationApproval entity = new DartCompensationApproval();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setSe(item.getSe());
        entity.setNmpr(support.safeParseLong(item.getNmpr()));
        entity.setGmtsckCnfrmAmount(support.safeParseLong(item.getGmtsckCnfrmAmount()));
        entity.setRm(item.getRm());
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
