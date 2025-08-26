package com.example.finalproject.apitest.service;

import com.example.finalproject.apitest.dto.material.response.DartBwIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCbIssuanceResponse;
import com.example.finalproject.apitest.dto.periodic.response.*;
import com.example.finalproject.apitest.entity.periodic.DartExecutiveStatus;

import java.io.IOException;
import java.util.List;

public interface TestService {
    List<DartMajorShareholderStatusResponse> DartMajorShareholderStatusCall(String corpCode, String reprtCode, String bsnsYear) throws IOException;
    List<DartMajorShareholderChangeResponse> DartMajorShareholderChangeCall(String corpCode, String reprtCode, String bsnsYear) throws IOException;
    //임원 현황
    List<DartExecutiveStatusResponse> DartExecutiveStatusCall(String corpCode, String reprtCode, String bsnsYear) throws IOException;

    List<DartCbIssuanceResponse> DartCbIssuanceCall(String corpCode, String bgnDe, String endDe) throws IOException;

    List<DartBwIssuanceResponse> DartBwIssuanceCall(String corpCode, String bgnDe, String endDe) throws IOException;

    List<DartTotalStockStatusResponse> DartTotalStockStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;

    List<DartTreasuryStockStatusResponse> DartTreasuryStockStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;

    List<DartSingleCompanyKeyAccountResponse> DartSingleCompanyKeyAccountCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;

    List<DartNonConsolidatedFinancialStatementResponse> DartNonConsolidatedFinancialStatementCall(String corpCode, String bsnsYear, String reprtCode, String fsDiv) throws IOException;

    List<DartCorporateBondBalanceResponse> DartCorporateBondBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;

    List<DartCommercialPaperBalanceResponse> DartCommercialPaperBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
}
