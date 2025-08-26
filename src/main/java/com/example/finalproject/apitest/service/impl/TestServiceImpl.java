package com.example.finalproject.apitest.service.impl;

import com.example.finalproject.apitest.dto.material.response.DartBwIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCbIssuanceResponse;
import com.example.finalproject.apitest.dto.periodic.response.DartExecutiveStatusResponse;
import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderChangeResponse;
import com.example.finalproject.apitest.dto.periodic.response.DartMajorShareholderStatusResponse;
import com.example.finalproject.apitest.service.material.DartBwIssuanceService;
import com.example.finalproject.apitest.service.material.DartCbIssuanceService;
import com.example.finalproject.apitest.service.periodic.DartExecutiveStatusService;
import com.example.finalproject.apitest.service.periodic.DartMajorShareholderChangeService;
import com.example.finalproject.apitest.service.periodic.DartMajorShareholderStatusService;
import com.example.finalproject.apitest.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final DartMajorShareholderStatusService dartMajorShareholderStatusService;
    private final DartMajorShareholderChangeService dartMajorShareholderChangeService;
    private final DartExecutiveStatusService dartExecutiveStatusService;
    private final DartCbIssuanceService dartCbIssuanceService;
    private final DartBwIssuanceService dartBwIssuanceService;


    @Override
    public List<DartMajorShareholderStatusResponse> DartMajorShareholderStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartMajorShareholderStatusService.dartMajorShareholderStatusCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartMajorShareholderChangeResponse> DartMajorShareholderChangeCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartMajorShareholderChangeService.dartMajorShareholderChangeCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartExecutiveStatusResponse> DartExecutiveStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartExecutiveStatusService.dartExecutiveStatusCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartCbIssuanceResponse> DartCbIssuanceCall(String corpCode, String bgnDe, String endDe) throws IOException {
        return dartCbIssuanceService.dartCbIssuanceCall(corpCode, bgnDe, endDe);
    }

    public List<DartBwIssuanceResponse> DartBwIssuanceCall(String corpCode, String bgnDe, String endDe) throws IOException {
        return dartBwIssuanceService.dartBwIssuanceCall(corpCode, bgnDe, endDe);
    }

}