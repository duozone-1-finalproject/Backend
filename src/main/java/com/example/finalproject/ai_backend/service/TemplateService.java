// src/main/java/com/example/ai_backend/service/TemplateService.java
package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.CompanyDataDto2;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TemplateService {

    /**
     * 기본 증권신고서 템플릿 반환
     */
    public String getSecuritiesRegistrationTemplate() {
        // 제공해주신 HTML 템플릿을 반환
        return """
                <!DOCTYPE html>
                <html style="border:0">
                <head>
                  <title></title>
                  <meta http-equiv="X-UA-Compatible" content="ie=edge">
                  <meta http-equiv="X-UA-TextLayoutMetrics" content="gdi">
                  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
                  <link rel="stylesheet" type="text/css" href="https://dart.fss.or.kr/css/report_xml.css">
                  <style>
                    span.highlight { background-color:#ffff00; }
                    span.highlight.on { background-color:#ffb700; }
                    .bookmark { background-color:#FFF0F0 !important; text-decoration:underline; }
                  </style>
                </head>
                <body bgcolor="#FFFFFF">
                  <div class="part">
                    <p><br></p>
                    <p class="cover-title"><a name="toc1">증 권 신 고 서</a></p>
                    <table class="nb" width="600">
                      <colgroup>
                        <col width="600">
                      </colgroup>
                      <tbody>
                        <tr>
                          <td width="600" height="20" align="CENTER">( 지 분 증 권 )</td>
                        </tr>
                      </tbody>
                    </table>
                    <p><br></p>
                    <p><br></p>
                    <table class="nb" width="600">
                      <colgroup>
                        <col width="262">
                        <col width="338">
                      </colgroup>
                      <tbody>
                        <tr>
                          <td width="262" height="20" valign="TOP">금융위원회 귀중</td>
                          <td width="338" height="20" align="RIGHT" valign="TOP">{{year}} &nbsp; &nbsp; &nbsp;{{month}} &nbsp; &nbsp; {{day}}</td>
                        </tr>
                        <tr>
                          <td width="262" height="43" valign="TOP">회 &nbsp; &nbsp; &nbsp; 사 &nbsp; &nbsp; &nbsp; 명 :<br><br></td>
                          <td width="338" height="43" valign="TOP">{{ company_name }}</td>
                        </tr>
                        <tr>
                          <td width="262" height="43" valign="TOP">대 &nbsp; 표 &nbsp; &nbsp; 이 &nbsp; 사 :<br><br></td>
                          <td width="338" height="43" valign="TOP">{{ ceo_name }}</td>
                        </tr>
                        <tr>
                          <td width="262" height="43" valign="TOP">본 &nbsp;점 &nbsp;소 &nbsp;재 &nbsp;지 :<br><br></td>
                          <td width="338" height="43" valign="TOP">{{ address }}</td>
                        </tr>
                        <tr>
                          <td width="262" height="20" valign="TOP"><br></td>
                          <td width="338" height="20" valign="TOP">(전 &nbsp;화) {{ company_phone }}</td>
                        </tr>
                        <tr>
                          <td width="262" height="20" valign="TOP"><br></td>
                          <td width="338" height="20" valign="TOP">(홈페이지) {{ company_website }}</td>
                        </tr>
                        <!-- 더 많은 템플릿 내용... -->
                      </tbody>
                    </table>
                  </div>
                </body>
                </html>
                """;
    }

    /**
     * 템플릿에 회사 데이터를 매핑하여 변수 맵 생성
     */
    public Map<String, String> createTemplateVariables(CompanyDataDto2 companyData) {
        Map<String, String> variables = new HashMap<>();

        // 현재 날짜 정보
        java.time.LocalDate now = java.time.LocalDate.now();
        variables.put("year", String.valueOf(now.getYear()));
        variables.put("month", String.format("%02d", now.getMonthValue()));
        variables.put("day", String.format("%02d", now.getDayOfMonth()));

        // 회사 정보 매핑
        variables.put("company_name", companyData.getCorpName() != null ? companyData.getCorpName() : "");
        variables.put("ceo_name", companyData.getCeoName() != null ? companyData.getCeoName() : "");
        variables.put("address", companyData.getAddress() != null ? companyData.getAddress() : "");
        variables.put("company_phone", companyData.getPhoneNo() != null ? companyData.getPhoneNo() : "");
        variables.put("company_website", companyData.getHomeUrl() != null ? companyData.getHomeUrl() : "");
        variables.put("stock_type", "보통주"); // 기본값
        variables.put("stock_amount", companyData.getStockCode() != null ? companyData.getStockCode() : "");
        variables.put("total_amount", "미정"); // 실제 구현시 계산 로직 추가
        variables.put("doc_url", "http://dart.fss.or.kr");
        variables.put("author_position", "재무담당임원");
        variables.put("author_name", "홍길동"); // 기본값
        variables.put("author_phone", companyData.getPhoneNo() != null ? companyData.getPhoneNo() : "");

        // 공지사항 기본값
        variables.put("notice_investor", "본 증권의 투자에는 위험이 따르므로 투자 전 투자설명서를 반드시 참고하시기 바랍니다.");
        variables.put("notice_forecast", "본 문서에 포함된 예측정보는 현재 시점에서의 판단을 반영한 것으로 실제 결과와 다를 수 있습니다.");
        variables.put("notice_etc", "추가 공지사항이 있을 경우 회사 홈페이지를 통해 안내드립니다.");

        log.info("템플릿 변수 생성 완료: {}", variables.keySet());
        return variables;
    }

    /**
     * 템플릿에 변수를 적용하여 최종 HTML 생성
     */
    public String applyVariablesToTemplate(String template, Map<String, String> variables) {
        String result = template;

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            result = result.replace(placeholder, entry.getValue());
        }

        log.info("템플릿 변수 적용 완료");
        return result;
    }
}