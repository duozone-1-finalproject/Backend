package com.example.finalproject.apitest.service;

import com.example.finalproject.apitest.dto.periodic.response.DartExecutiveStatusResponse;
import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderChangeResponse;
import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderStatusResponse;
import com.example.finalproject.apitest.entity.periodic.DartExecutiveStatus;

import java.io.IOException;
import java.util.List;

public interface TestService {
    List<DartMajorShareholderStatusResponse> DartMajorShareholderStatusCall(String corpCode, String reprtCode, String bsnsYear) throws IOException;
    List<DartMajorShareholderChangeResponse> DartMajorShareholderChangeCall(String corpCode, String reprtCode, String bsnsYear) throws IOException;
    //임원 현황
    List<DartExecutiveStatusResponse> DartExecutiveStatusCall(String corpCode, String reprtCode, String bsnsYear) throws IOException;
}
