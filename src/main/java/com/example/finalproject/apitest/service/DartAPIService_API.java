
package com.example.finalproject.apitest.service;

import java.util.Map;

public interface DartAPIService_API {
    // DART API 데이터 수집 및 저장
    void fetchAndSaveSecuritiesData(String corpCode, String beginDe, String endDe);

    // 회사별 데이터 조회
    Map<String, Object> getCompanyData(String corpCode);

    // 접수번호별 데이터 조회
    Map<String, Object> getDataByRceptNo(String rceptNo);
}