package com.example.finalproject.apitest.service.equity;

import com.example.finalproject.apitest.dto.equity.external.*;
import com.example.finalproject.apitest.dto.equity.response.*;
import com.example.finalproject.apitest.entity.equity.*;
import com.example.finalproject.apitest.exception.DartApiException;
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
    public DartEquitySecuritiesResponse dartEquitySecuritiesCall(String corpCode, String bgnDe, String endDe) {
        // DB에 데이터가 있는지 먼저 확인 (대표로 general_info 테이블만 확인)
        if (!generalInfoRepo.findByCorpCode(corpCode).isEmpty()) {
            log.info("corpCode {}에 대한 지분증권 데이터가 이미 존재하여 DB에서 반환합니다.", corpCode);
            return buildResponseFromDb(corpCode);
        }

        // API 호출
        var responseType = new ParameterizedTypeReference<DartEquitySecuritiesApiResponse>() {};
        DartEquitySecuritiesApiResponse apiResponse = dartApiCaller.callGrouped(
                builder -> builder.path("/estkRs.json")
                        .queryParam("corp_code", corpCode)
                        .queryParam("bgn_de", bgnDe)
                        .queryParam("end_de", endDe),
                responseType
        );

        // API 응답 유효성 검사
        if (apiResponse == null || !"000".equals(apiResponse.getStatus())) {
            String status = (apiResponse != null) ? apiResponse.getStatus() : "null";
            String message = (apiResponse != null) ? apiResponse.getMessage() : "null response";
            throw new DartApiException("DART API 에러: status=" + status + ", message=" + message);
        }

        if (apiResponse.getGroups() == null || apiResponse.getGroups().isEmpty()) {
            return DartEquitySecuritiesResponse.builder().build(); // 빈 응답 반환
        }

        // 각 그룹을 순회하며 데이터 처리 및 저장
        apiResponse.getGroups().forEach(this::processAndSaveGroup);

        // 저장된 데이터를 DB에서 다시 읽어와 최종 응답 생성
        return buildResponseFromDb(corpCode);
    }

    /**
     * API 응답의 각 그룹을 파싱하고 DB에 저장하는 메소드
     */
    private void processAndSaveGroup(EquityGroupDto group) {
        String title = group.getTitle();
        List<JsonNode> list = group.getList();
        if (list == null || list.isEmpty()) return;

        try {
            switch (title) {
                case "일반사항":
                    List<EquityGeneralInfoItem> generalItems = objectMapper.convertValue(list, new TypeReference<>() {});
                    generalInfoRepo.saveAll(generalItems.stream().map(this::toGeneralInfoEntity).collect(Collectors.toList()));
                    break;
                case "증권의종류":
                    List<EquitySecurityTypeItem> securityItems = objectMapper.convertValue(list, new TypeReference<>() {});
                    securityTypeRepo.saveAll(securityItems.stream().map(this::toSecurityTypeEntity).collect(Collectors.toList()));
                    break;
                case "인수인정보":
                    List<EquityUnderwriterInfoItem> underwriterItems = objectMapper.convertValue(list, new TypeReference<>() {});
                    underwriterInfoRepo.saveAll(underwriterItems.stream().map(this::toUnderwriterInfoEntity).collect(Collectors.toList()));
                    break;
                case "자금의사용목적":
                    List<EquityFundUsageItem> fundUsageItems = objectMapper.convertValue(list, new TypeReference<>() {});
                    fundUsageRepo.saveAll(fundUsageItems.stream().map(this::toFundUsageEntity).collect(Collectors.toList()));
                    break;
                case "매출인에관한사항":
                    List<EquitySellerInfoItem> sellerInfoItems = objectMapper.convertValue(list, new TypeReference<>() {});
                    sellerInfoRepo.saveAll(sellerInfoItems.stream().map(this::toSellerInfoEntity).collect(Collectors.toList()));
                    break;
                case "일반청약자환매청구권":
                    List<EquityRepurchaseOptionItem> repurchaseItems = objectMapper.convertValue(list, new TypeReference<>() {});
                    repurchaseOptionRepo.saveAll(repurchaseItems.stream().map(this::toRepurchaseOptionEntity).collect(Collectors.toList()));
                    break;
                default:
                    log.warn("알 수 없는 지분증권 그룹 타이틀입니다: {}", title);
            }
        } catch (IllegalArgumentException e) {
            log.error("그룹 '{}'의 리스트 파싱 중 에러 발생", title, e);
        }
    }

    /**
     * DB에 저장된 6종류의 데이터를 읽어와 최종 응답 DTO를 조립하는 메소드
     */
    private DartEquitySecuritiesResponse buildResponseFromDb(String corpCode) {
        List<EquityGeneralInfoResponse> generalInfos = generalInfoRepo.findByCorpCode(corpCode).stream()
                .map(EquityGeneralInfoResponse::from).collect(Collectors.toList());
        List<EquitySecurityTypeResponse> securityTypes = securityTypeRepo.findByCorpCode(corpCode).stream()
                .map(EquitySecurityTypeResponse::from).collect(Collectors.toList());
        List<EquityFundUsageResponse> fundUsages = fundUsageRepo.findByCorpCode(corpCode).stream()
                .map(EquityFundUsageResponse::from).collect(Collectors.toList());
        List<EquitySellerInfoResponse> sellerInfos = sellerInfoRepo.findByCorpCode(corpCode).stream()
                .map(EquitySellerInfoResponse::from).collect(Collectors.toList());
        List<EquityUnderwriterInfoResponse> underwriterInfos = underwriterInfoRepo.findByCorpCode(corpCode).stream()
                .map(EquityUnderwriterInfoResponse::from).collect(Collectors.toList());
        List<EquityRepurchaseOptionResponse> repurchaseOptions = repurchaseOptionRepo.findByCorpCode(corpCode).stream()
                .map(EquityRepurchaseOptionResponse::from).collect(Collectors.toList());

        return DartEquitySecuritiesResponse.builder()
                .generalInfos(generalInfos)
                .securityTypes(securityTypes)
                .fundUsages(fundUsages)
                .sellerInfos(sellerInfos)
                .underwriterInfos(underwriterInfos)
                .repurchaseOptions(repurchaseOptions)
                .build();
    }

    // --- Item DTO를 Entity로 변환하는 메소드들 ---

    private EquityGeneralInfo toGeneralInfoEntity(EquityGeneralInfoItem item) {
        EquityGeneralInfo entity = new EquityGeneralInfo();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setSbd(support.safeParseLocalDate(item.getSbd(), "yyyy년 MM월 dd일"));
        entity.setPymd(support.safeParseLocalDate(item.getPymd(), "yyyy년 MM월 dd일"));
        entity.setSband(support.safeParseLocalDate(item.getSband(), "yyyy년 MM월 dd일"));
        entity.setAsand(support.safeParseLocalDate(item.getAsand(), "yyyy년 MM월 dd일"));
        entity.setAsstd(support.safeParseLocalDate(item.getAsstd(), "yyyy년 MM월 dd일"));
        entity.setExstk(item.getExstk());
        entity.setExprc(support.safeParseLong(item.getExprc()));
        entity.setExpd(item.getExpd());
        entity.setRptRcpn(item.getRptRcpn());
        return entity;
    }

    private EquitySecurityType toSecurityTypeEntity(EquitySecurityTypeItem item) {
        EquitySecurityType entity = new EquitySecurityType();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setStksen(item.getStksen());
        entity.setStkcnt(support.safeParseLong(item.getStkcnt()));
        entity.setFv(support.safeParseLong(item.getFv()));
        entity.setSlprc(support.safeParseLong(item.getSlprc()));
        entity.setSlta(support.safeParseLong(item.getSlta()));
        entity.setSlmthn(item.getSlmthn());
        return entity;
    }

    private EquityUnderwriterInfo toUnderwriterInfoEntity(EquityUnderwriterInfoItem item) {
        EquityUnderwriterInfo entity = new EquityUnderwriterInfo();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setActsen(item.getActsen());
        entity.setActnmn(item.getActnmn());
        entity.setStksen(item.getStksen());
        entity.setUdtcnt(support.safeParseLong(item.getUdtcnt()));
        entity.setUdtamt(support.safeParseLong(item.getUdtamt()));
        entity.setUdtprc(support.safeParseLong(item.getUdtprc()));
        entity.setUdtmth(item.getUdtmth());
        return entity;
    }

    private EquityFundUsage toFundUsageEntity(EquityFundUsageItem item) {
        EquityFundUsage entity = new EquityFundUsage();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setSe(item.getSe());
        entity.setAmt(support.safeParseLong(item.getAmt()));
        return entity;
    }

    private EquitySellerInfo toSellerInfoEntity(EquitySellerInfoItem item) {
        EquitySellerInfo entity = new EquitySellerInfo();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setHdr(item.getHdr());
        entity.setRlCmp(item.getRlCmp());
        entity.setBfslHdstk(support.safeParseLong(item.getBfslHdstk()));
        entity.setSlstk(support.safeParseLong(item.getSlstk()));
        entity.setAtslHdstk(support.safeParseLong(item.getAtslHdstk()));
        return entity;
    }

    private EquityRepurchaseOption toRepurchaseOptionEntity(EquityRepurchaseOptionItem item) {
        EquityRepurchaseOption entity = new EquityRepurchaseOption();
        entity.setRceptNo(item.getRceptNo());
        entity.setCorpCls(item.getCorpCls());
        entity.setCorpCode(item.getCorpCode());
        entity.setCorpName(item.getCorpName());
        entity.setGrtrs(item.getGrtrs());
        entity.setExavivr(item.getExavivr());
        entity.setGrtcnt(support.safeParseLong(item.getGrtcnt()));
        entity.setExpd(item.getExpd());
        entity.setExprc(support.safeParseLong(item.getExprc()));
        return entity;
    }
}

