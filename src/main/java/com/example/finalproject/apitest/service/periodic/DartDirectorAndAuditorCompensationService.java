package com.example.finalproject.apitest.service.periodic;

import com.example.finalproject.apitest.dto.common.DartApiResponseDto;
import com.example.finalproject.apitest.dto.periodic.external.DartDirectorAndAuditorCompensationItem;
import com.example.finalproject.apitest.dto.periodic.response.DartDirectorAndAuditorCompensationResponse;
import com.example.finalproject.apitest.entity.periodic.DartDirectorAndAuditorCompensation;
import com.example.finalproject.apitest.repository.periodic.DartDirectorAndAuditorCompensationRepository;
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
public class DartDirectorAndAuditorCompensationService {

    private final DartApiCaller dartApiCaller;
    private final DartDirectorAndAuditorCompensationRepository repository;
    private final Support support;

    @Transactional
    public List<DartDirectorAndAuditorCompensationResponse> dartDirectorAndAuditorCompensationCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        List<DartDirectorAndAuditorCompensation> existingData = repository.findByCorpCode(corpCode);
        if (!existingData.isEmpty()) {
            log.info("corpCode {}에 대한 이사/감사 보수지급금액 현황 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return existingData.stream()
                    .map(DartDirectorAndAuditorCompensationResponse::from)
                    .collect(Collectors.toList());
        }

        var responseType = new ParameterizedTypeReference<DartApiResponseDto<DartDirectorAndAuditorCompensationItem>>() {};
        List<DartDirectorAndAuditorCompensationItem> items = dartApiCaller.call(
                builder -> builder.path("/drctrAdtAllMendngSttusMendngPymntamtTyCl.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bsns_year", bsnsYear)
                        .queryParam("reprt_code", reprtCode),
                responseType
        );

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<DartDirectorAndAuditorCompensation> entitiesToSave = items.stream()
                .map(this::mapItemToEntity)
                .collect(Collectors.toList());

        List<DartDirectorAndAuditorCompensation> savedEntities = repository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(DartDirectorAndAuditorCompensationResponse::from)
                .collect(Collectors.toList());
    }

    private DartDirectorAndAuditorCompensation mapItemToEntity(DartDirectorAndAuditorCompensationItem item) {
        DartDirectorAndAuditorCompensation entity = new DartDirectorAndAuditorCompensation();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setSe(item.getSe());
        entity.setNmpr(support.safeParseLong(item.getNmpr()));
        entity.setPymntTotamt(support.safeParseLong(item.getPymntTotamt()));
        entity.setPsn1AvrgPymntamt(support.safeParseLong(item.getPsn1AvrgPymntamt()));
        entity.setRm(item.getRm());
        entity.setStlmDt(support.safeParseLocalDate(item.getStlmDt()));
        return entity;
    }
}
