package com.example.apitest.service;

import com.example.apitest.entity.*;
import com.example.apitest.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DartApiServiceImpl implements DartFetchUseCase, DartQueryUseCase {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final DartGeneralInfoRepository generalInfoRepository;
    private final DartSecuritiesInfoRepository securitiesInfoRepository;
    private final DartUnderwriterInfoRepository underwriterInfoRepository;
    private final DartFundUsageRepository fundUsageRepository;
    private final DartSellerInfoRepository sellerInfoRepository;
    private final DartRedemptionRightsRepository redemptionRightsRepository;

    @Value("${dart.api.key}")
    private String dartApiKey;

    @Value("${dart.api.base-url}")
    private String dartApiBaseUrl;

    /** FETCH: DART API 호출 + 저장 */
    @Override
    @Transactional
    public void fetchAndSaveSecuritiesData(String corpCode, String beginDe, String endDe) {
        try {
            // (옵션) 공시 목록 조회 - 로그/대상 식별용
            String listUrl = String.format("%s/list.json?crtfc_key=%s&corp_code=%s&bgn_de=%s&end_de=%s",
                    dartApiBaseUrl, dartApiKey, corpCode, beginDe, endDe);
            log.info("공시목록 호출: {}", listUrl);
            try {
                String listResp = restTemplate.getForObject(listUrl, String.class);
                if (listResp != null && !listResp.isBlank()) {
                    JsonNode listRoot = objectMapper.readTree(listResp);
                    if ("000".equals(listRoot.path("status").asText())) {
                        JsonNode listNode = listRoot.path("list");
                        log.info("공시 목록 처리: {}건", listNode.size());
                        for (JsonNode item : listNode) {
                            log.info("공시 정보: 접수번호={}, 회사명={}, 보고서명={}, 접수일자={}",
                                    item.path("rcept_no").asText(),
                                    item.path("corp_name").asText(),
                                    item.path("report_nm").asText(),
                                    item.path("rcept_dt").asText());
                        }
                    } else {
                        log.warn("공시 목록 조회 실패: {} - {}",
                                listRoot.path("status").asText(),
                                listRoot.path("message").asText());
                    }
                }
            } catch (Exception ignore) {
                log.warn("공시 목록 조회 중 경고(저장과 무관): {}", ignore.getMessage());
            }

            // ★ 핵심: 지분증권 주요정보 호출 (실제 저장할 그룹 데이터가 나옴)
            // base-url 이 .../api 라면 뒤에는 estkRs.json / list.json 처럼 붙입니다.
            String estkUrl = String.format("%s/estkRs.json?crtfc_key=%s&corp_code=%s&bgn_de=%s&end_de=%s",
                    dartApiBaseUrl, dartApiKey, corpCode, beginDe, endDe);
            log.info("지분증권 주요정보 호출: {}", estkUrl);

            String estkResp = restTemplate.getForObject(estkUrl, String.class);
            if (estkResp == null || estkResp.isBlank()) {
                log.warn("estkRs 응답이 비어있습니다.");
                return;
            }

            JsonNode root = objectMapper.readTree(estkResp);
            String status = root.path("status").asText();
            if ("013".equals(status)) { // 조회 데이터 없음
                log.info("estkRs: 해당 기간({}~{})에 데이터가 없습니다.", beginDe, endDe);
                return;
            }
            if (!"000".equals(status)) {
                log.error("estkRs 오류: {} - {}", status, root.path("message").asText());
                return;
            }

            // 응답 형태 2종 대응: group 배열 또는 {일반사항:{grouptitle,list}, ...}
            if (root.has("group") && root.get("group").isArray()) {
                parseAndSaveFromGroupArray(root.get("group"));
            } else {
                parseAndSaveData(root);
            }

            log.info("DART API 저장 완료");

        } catch (Exception e) {
            log.error("DART API 처리 오류", e);
            throw new RuntimeException("DART API 데이터 처리 실패", e);
        }
    }

    /** list.json 응답 구조에 맞는 파싱 */
    private void parseListData(JsonNode rootNode) {
        JsonNode listNode = rootNode.path("list");
        if (!listNode.isArray() || listNode.size() == 0) {
            log.info("조회된 공시 데이터가 없습니다.");
            return;
        }

        log.info("공시 목록 처리: {}건", listNode.size());

        // 각 공시 건에 대해 상세 정보를 가져와야 한다면 추가 API 호출 필요
        for (JsonNode item : listNode) {
            String rceptNo = item.path("rcept_no").asText();
            String corpName = item.path("corp_name").asText();
            String reportNm = item.path("report_nm").asText();
            String rceptDt = item.path("rcept_dt").asText();

            log.info("공시 정보: 접수번호={}, 회사명={}, 보고서명={}, 접수일자={}",
                    rceptNo, corpName, reportNm, rceptDt);

            // 필요한 경우 각 접수번호별로 상세 정보 저장
            // savePublicDisclosureInfo(item);
        }
    }

    private void parseAndSaveFromGroupArray(JsonNode groupArray) {
        for (JsonNode g : groupArray) {
            String groupTitle = g.path("title").asText();
            JsonNode listNode = g.path("list");
            if (listNode.isArray() && listNode.size() > 0) {
                processGroupData(groupTitle, listNode);
            }
        }
    }

    /** 루트 JSON 순회 → 그룹별 저장 */
    private void parseAndSaveData(JsonNode rootNode) {
        Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> f = fields.next();
            String key = f.getKey();
            if ("status".equals(key) || "message".equals(key)) continue;

            JsonNode v = f.getValue();
            String groupTitle = v.path("grouptitle").asText();
            JsonNode listNode = v.path("list");
            if (listNode.isArray() && listNode.size() > 0) {
                processGroupData(groupTitle, listNode);
            }
        }
    }

    /** 그룹 분기 */
    private void processGroupData(String groupTitle, JsonNode listNode) {
        log.info("그룹 처리: {} ({}건)", groupTitle, listNode.size());
        switch (groupTitle) {
            case "일반사항" -> saveGeneralInfo(listNode);
            case "증권의종류" -> saveSecuritiesInfo(listNode);
            case "인수인정보" -> saveUnderwriterInfo(listNode);
            case "자금의사용목적" -> saveFundUsage(listNode);
            case "매출인에관한사항" -> saveSellerInfo(listNode);
            case "일반청약자환매청구권" -> saveRedemptionRights(listNode);
            default -> log.warn("미지원 그룹: {}", groupTitle);
        }
    }

    /** 일반사항 */
    private void saveGeneralInfo(JsonNode listNode) {
        List<DartGeneralInfo> list = new ArrayList<>();
        for (JsonNode item : listNode) {
            DartGeneralInfo e = new DartGeneralInfo();
            e.setRceptNo(item.path("rcept_no").asText(null));
            e.setCorpCls(item.path("corp_cls").asText(null));
            e.setCorpCode(item.path("corp_code").asText(null));
            e.setCorpName(item.path("corp_name").asText(null));
            e.setSbd(parseDate(item.path("sbd").asText(null)));
            e.setPymd(parseDate(item.path("pymd").asText(null)));
            e.setSband(parseDate(item.path("sband").asText(null)));
            e.setAsand(parseDate(item.path("asand").asText(null)));
            e.setAsstd(parseDate(item.path("asstd").asText(null)));
            e.setExstk(item.path("exstk").asText(null));
            e.setExprc(parseLong(item.path("exprc").asText(null)));
            e.setExpd(item.path("expd").asText(null));
            e.setRptRcpn(item.path("rpt_rcpn").asText(null));
            list.add(e);
        }
        for (DartGeneralInfo e : list) {
            if (e.getRceptNo() != null && !generalInfoRepository.existsByRceptNo(e.getRceptNo())) {
                generalInfoRepository.save(e);
            }
        }
        log.info("일반사항 저장: {}건", list.size());
    }

    /** 증권의종류 */
    private void saveSecuritiesInfo(JsonNode listNode) {
        List<DartSecuritiesInfo> list = new ArrayList<>();
        for (JsonNode item : listNode) {
            DartSecuritiesInfo e = new DartSecuritiesInfo();
            e.setRceptNo(item.path("rcept_no").asText(null));
            e.setCorpCls(item.path("corp_cls").asText(null));
            e.setCorpCode(item.path("corp_code").asText(null));
            e.setCorpName(item.path("corp_name").asText(null));
            e.setStksen(item.path("stksen").asText(null));
            e.setStkcnt(parseLong(item.path("stkcnt").asText(null)));
            e.setFv(parseLong(item.path("fv").asText(null)));
            e.setSlprc(parseLong(item.path("slprc").asText(null)));
            e.setSlta(parseLong(item.path("slta").asText(null)));
            e.setSlmthn(item.path("slmthn").asText(null));
            list.add(e);
        }
        securitiesInfoRepository.saveAll(list);
        log.info("증권의종류 저장: {}건", list.size());
    }

    /** 인수인정보 */
    private void saveUnderwriterInfo(JsonNode listNode) {
        List<DartUnderwriterInfo> list = new ArrayList<>();
        for (JsonNode item : listNode) {
            DartUnderwriterInfo e = new DartUnderwriterInfo();
            e.setRceptNo(item.path("rcept_no").asText(null));
            e.setCorpCls(item.path("corp_cls").asText(null));
            e.setCorpCode(item.path("corp_code").asText(null));
            e.setCorpName(item.path("corp_name").asText(null));
            e.setActsen(item.path("actsen").asText(null));
            e.setActnmn(item.path("actnmn").asText(null));
            e.setStksen(item.path("stksen").asText(null));
            e.setUdtcnt(parseLong(item.path("udtcnt").asText(null)));
            e.setUdtamt(parseLong(item.path("udtamt").asText(null)));
            e.setUdtprc(parseLong(item.path("udtprc").asText(null)));
            e.setUdtmth(item.path("udtmth").asText(null));
            list.add(e);
        }
        underwriterInfoRepository.saveAll(list);
        log.info("인수인정보 저장: {}건", list.size());
    }

    /** 자금의사용목적 */
    private void saveFundUsage(JsonNode listNode) {
        List<DartFundUsage> list = new ArrayList<>();
        for (JsonNode item : listNode) {
            DartFundUsage e = new DartFundUsage();
            e.setRceptNo(item.path("rcept_no").asText(null));
            e.setCorpCls(item.path("corp_cls").asText(null));
            e.setCorpCode(item.path("corp_code").asText(null));
            e.setCorpName(item.path("corp_name").asText(null));
            e.setSe(item.path("se").asText(null));
            e.setAmt(parseLong(item.path("amt").asText(null)));
            list.add(e);
        }
        fundUsageRepository.saveAll(list);
        log.info("자금의사용목적 저장: {}건", list.size());
    }

    /** 매출인에관한사항 */
    private void saveSellerInfo(JsonNode listNode) {
        List<DartSellerInfo> list = new ArrayList<>();
        for (JsonNode item : listNode) {
            DartSellerInfo e = new DartSellerInfo();
            e.setRceptNo(item.path("rcept_no").asText(null));
            e.setCorpCls(item.path("corp_cls").asText(null));
            e.setCorpCode(item.path("corp_code").asText(null));
            e.setCorpName(item.path("corp_name").asText(null));
            e.setHdr(item.path("hdr").asText(null));
            e.setRlCmp(item.path("rl_cmp").asText(null));
            e.setBfslHdstk(parseLong(item.path("bfsl_hdstk").asText(null)));
            e.setSlstk(parseLong(item.path("slstk").asText(null)));
            e.setAtslHdstk(parseLong(item.path("atsl_hdstk").asText(null)));
            list.add(e);
        }
        sellerInfoRepository.saveAll(list);
        log.info("매출인에관한사항 저장: {}건", list.size());
    }

    /** 일반청약자환매청구권 */
    private void saveRedemptionRights(JsonNode listNode) {
        List<DartRedemptionRights> list = new ArrayList<>();
        for (JsonNode item : listNode) {
            DartRedemptionRights e = new DartRedemptionRights();
            e.setRceptNo(item.path("rcept_no").asText(null));
            e.setCorpCls(item.path("corp_cls").asText(null));
            e.setCorpCode(item.path("corp_code").asText(null));
            e.setCorpName(item.path("corp_name").asText(null));
            e.setGrtrs(item.path("grtrs").asText(null));
            e.setExavivr(item.path("exavivr").asText(null));
            e.setGrtcnt(parseLong(item.path("grtcnt").asText(null)));
            e.setExpd(item.path("expd").asText(null));
            e.setExprc(parseLong(item.path("exprc").asText(null)));
            list.add(e);
        }
        redemptionRightsRepository.saveAll(list);
        log.info("일반청약자환매청구권 저장: {}건", list.size());
    }

    /** QUERY 계열 */
    @Override
    public Map<String, Object> getCompanyData(String corpCode) {
        return Map.of(
                "generalInfo", generalInfoRepository.findByCorpCode(corpCode),
                "securitiesInfo", securitiesInfoRepository.findByCorpCode(corpCode),
                "underwriterInfo", underwriterInfoRepository.findByCorpCode(corpCode),
                "fundUsage", fundUsageRepository.findByCorpCode(corpCode),
                "sellerInfo", sellerInfoRepository.findByCorpCode(corpCode),
                "redemptionRights", redemptionRightsRepository.findByCorpCode(corpCode)
        );
    }

    @Override
    public Map<String, Object> getDataByRceptNo(String rceptNo) {
        return Map.of(
                "generalInfo", generalInfoRepository.findByRceptNo(rceptNo),
                "securitiesInfo", securitiesInfoRepository.findByRceptNo(rceptNo),
                "underwriterInfo", underwriterInfoRepository.findByRceptNo(rceptNo),
                "fundUsage", fundUsageRepository.findByRceptNo(rceptNo),
                "sellerInfo", sellerInfoRepository.findByRceptNo(rceptNo),
                "redemptionRights", redemptionRightsRepository.findByRceptNo(rceptNo)
        );
    }

    /** Utils */
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty() || "-".equals(dateStr)) return null;
        try {
            if (dateStr.contains("년") && dateStr.contains("월") && dateStr.contains("일")) {
                // 공백이 있든 없든 대응
                dateStr = dateStr.replace("년", "-").replace("월", "-").replace("일", "")
                        .replace(" ", "");
            }
            return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            log.warn("날짜 파싱 실패: {}", dateStr);
            return null;
        }
    }

    private Long parseLong(String str) {
        if (str == null || str.trim().isEmpty() || "-".equals(str) || "9,999,999,999".equals(str)) return null;
        try {
            return Long.parseLong(str.replace(",", "").trim());
        } catch (NumberFormatException e) {
            log.warn("숫자 파싱 실패: {}", str);
            return null;
        }
    }
}
