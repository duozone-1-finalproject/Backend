package com.example.finalproject.apitest.service;

import com.example.finalproject.apitest.dto.material.response.DartBwIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCbIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCocoBondIssuanceResponse;
import com.example.finalproject.apitest.dto.overview.response.DartCompanyOverviewResponse;
import com.example.finalproject.apitest.dto.periodic.response.*;

import java.io.IOException;
import java.util.List;

public interface TestService {
    List<DartMajorShareholderStatusResponse> DartMajorShareholderStatusCall(String corpCode, String reprtCode, String bsnsYear) throws IOException;
    List<DartMajorShareholderChangeResponse> DartMajorShareholderChangeCall(String corpCode, String reprtCode, String bsnsYear) throws IOException;
    List<DartExecutiveStatusResponse> DartExecutiveStatusCall(String corpCode, String reprtCode, String bsnsYear) throws IOException;
    List<DartEmployeeStatusResponse> DartEmployeeStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartUnregisteredExecutiveCompensationResponse> DartUnregisteredExecutiveCompensationCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartCbIssuanceResponse> DartCbIssuanceCall(String corpCode, String bgnDe, String endDe) throws IOException;
    List<DartBwIssuanceResponse> DartBwIssuanceCall(String corpCode, String bgnDe, String endDe) throws IOException;
    List<DartCocoBondIssuanceResponse> DartCocoBondIssuanceCall(String corpCode, String bgnDe, String endDe) throws IOException;
    List<DartPublicOfferingFundUsageResponse> DartPublicOfferingFundUsageCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartPrivatePlacementFundUsageResponse> DartPrivatePlacementFundUsageCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartAuditOpinionResponse> DartAuditOpinionCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartAuditServiceContractResponse> DartAuditServiceContractCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartNonAuditServiceContractResponse> DartNonAuditServiceContractCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartOutsideDirectorChangeStatusResponse> DartOutsideDirectorChangeStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartTotalStockStatusResponse> DartTotalStockStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartTreasuryStockStatusResponse> DartTreasuryStockStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartSingleCompanyKeyAccountResponse> DartSingleCompanyKeyAccountCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartNonConsolidatedFinancialStatementResponse> DartNonConsolidatedFinancialStatementCall(String corpCode, String bsnsYear, String reprtCode, String fsDiv) throws IOException;
    List<DartCorporateBondBalanceResponse> DartCorporateBondBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartCommercialPaperBalanceResponse> DartCommercialPaperBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartShortTermBondBalanceResponse> DartShortTermBondBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartHybridSecuritiesBalanceResponse> DartHybridSecuritiesBalanceCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartMinorityShareholderStatusResponse> DartMinorityShareholderStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartCompensationApprovalResponse> DartCompensationApprovalCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    List<DartDirectorAndAuditorCompensationResponse> DartDirectorAndAuditorCompensationCall(String corpCode, String bsnsYear, String reprtCode) throws IOException;
    DartCompanyOverviewResponse DartCompanyOverviewCall(String corpCode) throws IOException;
}
