package com.example.finalproject.apitest.service.impl;

import com.example.finalproject.apitest.dto.material.response.DartBwIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCbIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCocoBondIssuanceResponse;
import com.example.finalproject.apitest.dto.periodic.response.*;
import com.example.finalproject.apitest.service.material.DartBwIssuanceService;
import com.example.finalproject.apitest.service.material.DartCbIssuanceService;
import com.example.finalproject.apitest.service.material.DartCocoBondIssuanceService;
import com.example.finalproject.apitest.service.periodic.*;
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
    private final DartCocoBondIssuanceService dartCocoBondIssuanceService;
    private final DartPublicOfferingFundUsageService dartPublicOfferingFundUsageService;
    private final DartPrivatePlacementFundUsageService dartPrivatePlacementFundUsageService; // [추가]
    private final DartTotalStockStatusService dartTotalStockStatusService;
    private final DartTreasuryStockStatusService dartTreasuryStockStatusService;
    private final DartSingleCompanyKeyAccountService dartSingleCompanyKeyAccountService;
    private final DartNonConsolidatedFinancialStatementService dartNonConsolidatedFinancialStatementService;
    private final DartCorporateBondBalanceService dartCorporateBondBalanceService;
    private final DartCommercialPaperBalanceService dartCommercialPaperBalanceService;
    private final DartShortTermBondBalanceService dartShortTermBondBalanceService;
    private final DartHybridSecuritiesBalanceService dartHybridSecuritiesBalanceService;


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

    @Override
    public List<DartBwIssuanceResponse> DartBwIssuanceCall(String corpCode, String bgnDe, String endDe) throws IOException {
        return dartBwIssuanceService.dartBwIssuanceCall(corpCode, bgnDe, endDe);
    }

    @Override
    public List<DartCocoBondIssuanceResponse> DartCocoBondIssuanceCall(String corpCode, String bgnDe, String endDe) throws IOException {
        return dartCocoBondIssuanceService.dartCocoBondIssuanceCall(corpCode, bgnDe, endDe);
    }

    @Override
    public List<DartPublicOfferingFundUsageResponse> DartPublicOfferingFundUsageCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartPublicOfferingFundUsageService.dartPublicOfferingFundUsageCall(corpCode, bsnsYear, reprtCode);
    }

    // [추가]
    @Override
    public List<DartPrivatePlacementFundUsageResponse> DartPrivatePlacementFundUsageCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartPrivatePlacementFundUsageService.dartPrivatePlacementFundUsageCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartTotalStockStatusResponse> DartTotalStockStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException{
        return dartTotalStockStatusService.dartTotalStockStatusCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartTreasuryStockStatusResponse> DartTreasuryStockStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException{
        return dartTreasuryStockStatusService.dartTreasuryStockStatusCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartSingleCompanyKeyAccountResponse> DartSingleCompanyKeyAccountCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartSingleCompanyKeyAccountService.dartSingleCompanyKeyAccountCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartNonConsolidatedFinancialStatementResponse> DartNonConsolidatedFinancialStatementCall(String corpCode, String bsnsYear, String reprtCode, String fsDiv) throws IOException {
        return dartNonConsolidatedFinancialStatementService.dartNonConsolidatedFinancialStatementCall(corpCode, bsnsYear, reprtCode, fsDiv);
    }

    @Override
    public List<DartCorporateBondBalanceResponse> DartCorporateBondBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartCorporateBondBalanceService.dartCorporateBondBalanceCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartCommercialPaperBalanceResponse> DartCommercialPaperBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartCommercialPaperBalanceService.dartCommercialPaperBalanceCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartShortTermBondBalanceResponse> DartShortTermBondBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartShortTermBondBalanceService.dartShortTermBondBalanceCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartHybridSecuritiesBalanceResponse> DartHybridSecuritiesBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartHybridSecuritiesBalanceService.dartHybridSecuritiesBalanceCall(corpCode, bsnsYear, reprtCode);
    }
}
