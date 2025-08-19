package com.example.finalproject.apitest.service;

import java.util.Map;

public interface DartQueryUseCase {
    Map<String, Object> getCompanyData(String corpCode);
    Map<String, Object> getDataByRceptNo(String rceptNo);
}
