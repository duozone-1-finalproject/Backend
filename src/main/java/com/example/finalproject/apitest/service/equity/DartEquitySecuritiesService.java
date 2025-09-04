package com.example.finalproject.apitest.service.equity;

import com.example.finalproject.apitest.dto.equity.external.*;
import com.example.finalproject.apitest.dto.equity.response.*;
import com.example.finalproject.apitest.entity.equity.*;
import com.example.finalproject.apitest.repository.equity.*;
import com.example.finalproject.apitest.service.common.DartApiCaller;
import com.example.finalproject.apitest.service.common.Support;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DartEquitySecuritiesService {

    private final DartApiCaller dartApiCaller;
    private final Support support;
    private final ObjectMapper objectMapper;

    // 6개의 Repository 주입
    private final EquityGeneralInfoRepository generalInfoRepo;
    private final EquitySecurityTypeRepository securityTypeRepo;
    private final EquityFundUsageRepository fundUsageRepo;
    private final EquitySellerInfoRepository sellerInfoRepo;
    private final EquityUnderwriterInfoRepository underwriterInfoRepo;
    private final EquityRepurchaseOptionRepository repurchaseOptionRepo;

    @Transactional
    public DartEquitySecuritiesGroupResponse dartEquitySecuritiesCall(String corpCode, String bgnDe, String endDe) {
        // [수정] 다른 서비스와 동일하게 중복 저장 방지 로직 추가
        if (!generalInfoRepo.findByCorpCode(corpCode).isEmpty()) {
            log.info("corpCode {}에 대한 지분증권 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return buildFinalResponse(corpCode);
        }

        var responseType = new ParameterizedTypeReference<DartEquitySecuritiesApiResponse>() {};

        DartEquitySecuritiesApiResponse apiResponse = dartApiCaller.callGrouped(
                builder -> builder.path("/estkRs.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bgn_de", bgnDe)
                        .queryParam("end_de", endDe),
                responseType
        );

        if (apiResponse == null || !"000".equals(apiResponse.getStatus()) || apiResponse.getGroups() == null || apiResponse.getGroups().isEmpty()) {
            log.info("corpCode {}에 대한 지분증권 데이터가 없습니다.", corpCode);
            return buildEmptyResponse();
        }

        apiResponse.getGroups().forEach(this::processGroup);
        return buildFinalResponse(corpCode);
    }

    private void processGroup(EquityGroupDto group) {
        String title = group.getTitle();
        JsonNode listNode = group.getList();
        if (title == null || listNode == null || !listNode.isArray()) return;

        try {
            switch (title) {
                case "일반사항":
                    List<EquityGeneralInfoItem> generalItems = objectMapper.convertValue(listNode, new TypeReference<>() {});
                    generalInfoRepo.saveAll(generalItems.stream().map(this::mapToGeneralInfoEntity).collect(Collectors.toList()));
                    break;
                case "증권의종류":
                    List<EquitySecurityTypeItem> securityItems = objectMapper.convertValue(listNode, new TypeReference<>() {});
                    securityTypeRepo.saveAll(securityItems.stream().map(this::mapToSecurityTypeEntity).collect(Collectors.toList()));
                    break;
                case "자금의사용목적":
                    List<EquityFundUsageItem> fundItems = objectMapper.convertValue(listNode, new TypeReference<>() {});
                    fundUsageRepo.saveAll(fundItems.stream().map(this::mapToFundUsageEntity).collect(Collectors.toList()));
                    break;
                case "매출인에관한사항":
                    List<EquitySellerInfoItem> sellerItems = objectMapper.convertValue(listNode, new TypeReference<>() {});
                    sellerInfoRepo.saveAll(sellerItems.stream().map(this::mapToSellerInfoEntity).collect(Collectors.toList()));
                    break;
                case "인수인정보":
                    List<EquityUnderwriterInfoItem> underwriterItems = objectMapper.convertValue(listNode, new TypeReference<>() {});
                    underwriterInfoRepo.saveAll(underwriterItems.stream().map(this::mapToUnderwriterInfoEntity).collect(Collectors.toList()));
                    break;
                case "일반청약자환매청구권":
                    List<EquityRepurchaseOptionItem> repurchaseItems = objectMapper.convertValue(listNode, new TypeReference<>() {});
                    repurchaseOptionRepo.saveAll(repurchaseItems.stream().map(this::mapToRepurchaseOptionEntity).collect(Collectors.toList()));
                    break;
                default:
                    log.warn("알 수 없는 지분증권 그룹 타이틀입니다: {}", title);
            }
        } catch (Exception e) {
            log.error("지분증권 그룹 파싱 또는 저장 중 오류 발생 (타이틀: {}): {}", title, e.getMessage());
            // [개선] 오류 발생 시 트랜잭션 롤백을 위해 예외를 다시 던져줍니다.
            throw new RuntimeException("Error processing group: " + title, e);
        }
    }

    // --- Entity 매핑 메소드들 ---

    private EquityGeneralInfo mapToGeneralInfoEntity(EquityGeneralInfoItem item) {
        EquityGeneralInfo entity = new EquityGeneralInfo();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setSbd(item.getSbd());
        entity.setPymd(support.safeParseLocalDate(safeTrim(item.getPymd()), "yyyy년 MM월 dd일"));
        entity.setSband(support.safeParseLocalDate(safeTrim(item.getSband()), "yyyy년 MM월 dd일"));
        entity.setAsand(support.safeParseLocalDate(safeTrim(item.getAsand()), "yyyy년 MM월 dd일"));
        entity.setAsstd(support.safeParseLocalDate(safeTrim(item.getAsstd()), "yyyy년 MM월 dd일"));
        entity.setExstk(item.getExstk());
        entity.setExprc(support.safeParseLong(safeTrim(item.getExprc())));
        entity.setExpd(item.getExpd());
        entity.setRptRcpn(item.getRptRcpn());
        return entity;
    }

    private EquitySecurityType mapToSecurityTypeEntity(EquitySecurityTypeItem item) {
        EquitySecurityType entity = new EquitySecurityType();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setStksen(item.getStksen());
        entity.setStkcnt(support.safeParseLong(safeTrim(item.getStkcnt())));
        entity.setFv(support.safeParseLong(safeTrim(item.getFv())));
        entity.setSlprc(support.safeParseLong(safeTrim(item.getSlprc())));
        entity.setSlta(support.safeParseLong(safeTrim(item.getSlta())));
        entity.setSlmthn(item.getSlmthn());
        return entity;
    }

    private EquityFundUsage mapToFundUsageEntity(EquityFundUsageItem item) {
        EquityFundUsage entity = new EquityFundUsage();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setSe(item.getSe());
        entity.setAmt(support.safeParseLong(safeTrim(item.getAmt())));
        return entity;
    }

    private EquitySellerInfo mapToSellerInfoEntity(EquitySellerInfoItem item) {
        EquitySellerInfo entity = new EquitySellerInfo();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setHdr(item.getHdr());
        entity.setRlCmp(item.getRlCmp());
        entity.setBfslHdstk(support.safeParseLong(safeTrim(item.getBfslHdstk())));
        entity.setSlstk(support.safeParseLong(safeTrim(item.getSlstk())));
        entity.setAtslHdstk(support.safeParseLong(safeTrim(item.getAtslHdstk())));
        return entity;
    }

    private EquityUnderwriterInfo mapToUnderwriterInfoEntity(EquityUnderwriterInfoItem item) {
        EquityUnderwriterInfo entity = new EquityUnderwriterInfo();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setStksen(item.getStksen());
        entity.setActsen(item.getActsen());
        entity.setActnmn(item.getActnmn());
        entity.setUdtcnt(support.safeParseLong(safeTrim(item.getUdtcnt())));
        entity.setUdtamt(support.safeParseLong(safeTrim(item.getUdtamt())));
        entity.setUdtprc(support.safeParseLong(safeTrim(item.getUdtprc())));
        entity.setUdtmth(item.getUdtmth());
        return entity;
    }

    private EquityRepurchaseOption mapToRepurchaseOptionEntity(EquityRepurchaseOptionItem item) {
        EquityRepurchaseOption entity = new EquityRepurchaseOption();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setGrtrs(item.getGrtrs());
        entity.setExavivr(item.getExavivr());
        entity.setGrtcnt(support.safeParseLong(safeTrim(item.getGrtcnt())));
        entity.setExpd(item.getExpd());
        entity.setExprc(support.safeParseLong(safeTrim(item.getExprc())));
        return entity;
    }

    // --- 유틸리티 및 응답 빌더 ---

    private String safeTrim(String text) {
        return StringUtils.hasText(text) ? text.trim() : null;
    }

    private DartEquitySecuritiesGroupResponse buildFinalResponse(String corpCode) {
        List<TitledGroup<?>> groups = new ArrayList<>();

        List<EquityGeneralInfoResponse> generalInfos = generalInfoRepo.findByCorpCode(corpCode)
                .stream().map(EquityGeneralInfoResponse::from).collect(Collectors.toList());
        if (!generalInfos.isEmpty()) {
            groups.add(new TitledGroup<>("일반사항", generalInfos));
        }

        List<EquitySecurityTypeResponse> securityTypes = securityTypeRepo.findByCorpCode(corpCode)
                .stream().map(EquitySecurityTypeResponse::from).collect(Collectors.toList());
        if (!securityTypes.isEmpty()) {
            groups.add(new TitledGroup<>("증권의종류", securityTypes));
        }

        List<EquityFundUsageResponse> fundUsages = fundUsageRepo.findByCorpCode(corpCode)
                .stream().map(EquityFundUsageResponse::from).collect(Collectors.toList());
        if (!fundUsages.isEmpty()) {
            groups.add(new TitledGroup<>("자금의사용목적", fundUsages));
        }

        List<EquitySellerInfoResponse> sellerInfos = sellerInfoRepo.findByCorpCode(corpCode)
                .stream().map(EquitySellerInfoResponse::from).collect(Collectors.toList());
        if (!sellerInfos.isEmpty()) {
            groups.add(new TitledGroup<>("매출인에관한사항", sellerInfos));
        }

        List<EquityUnderwriterInfoResponse> underwriterInfos = underwriterInfoRepo.findByCorpCode(corpCode)
                .stream().map(EquityUnderwriterInfoResponse::from).collect(Collectors.toList());
        if (!underwriterInfos.isEmpty()) {
            groups.add(new TitledGroup<>("인수인정보", underwriterInfos));
        }

        List<EquityRepurchaseOptionResponse> repurchaseOptions = repurchaseOptionRepo.findByCorpCode(corpCode)
                .stream().map(EquityRepurchaseOptionResponse::from).collect(Collectors.toList());
        if (!repurchaseOptions.isEmpty()) {
            groups.add(new TitledGroup<>("일반청약자환매청구권", repurchaseOptions));
        }

        return DartEquitySecuritiesGroupResponse.builder().group(groups).build();
    }

    private DartEquitySecuritiesGroupResponse buildEmptyResponse() {
        return DartEquitySecuritiesGroupResponse.builder()
                .group(Collections.emptyList())
                .build();
    }
}

