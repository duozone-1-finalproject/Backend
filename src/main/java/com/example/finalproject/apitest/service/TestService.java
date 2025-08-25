package com.example.finalproject.apitest.service;

import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderStatusResponse;

import java.io.IOException;
import java.util.List;

public interface TestService {
    List<DartMajorShareholderStatusResponse> testServ(String corpCode, String reprtCode, String bsnsYear) throws IOException;
}
