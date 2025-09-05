package com.example.finalproject.apitest.dto.common;

import com.example.finalproject.ai_backend.dto.CompanyDataDto2;
import com.example.finalproject.apitest.dto.material.response.DartBwIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCbIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCocoBondIssuanceResponse;
import com.example.finalproject.apitest.dto.overview.response.DartCompanyOverviewResponse;
import com.example.finalproject.apitest.dto.periodic.response.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AllDartDataResponse extends CompanyDataDto2 {
    // 각 API의 응답을 필드로 정의합니다.
    private DartCompanyOverviewResponse companyOverview;
    private List<DartMajorShareholderStatusResponse> majorShareholderStatus;
    private List<DartMajorShareholderChangeResponse> majorShareholderChange;
    private List<DartExecutiveStatusResponse> executiveStatus;
    private List<DartEmployeeStatusResponse> employeeStatus;
    private List<DartUnregisteredExecutiveCompensationResponse> unregisteredExecutiveCompensation;
    private List<DartCbIssuanceResponse> cbIssuance;
    private List<DartBwIssuanceResponse> bwIssuance;
    private List<DartCocoBondIssuanceResponse> cocoBondIssuance;
    private List<DartPublicOfferingFundUsageResponse> publicOfferingFundUsage;
    private List<DartPrivatePlacementFundUsageResponse> privatePlacementFundUsage;
    private List<DartAuditOpinionResponse> auditOpinion;
    private List<DartAuditServiceContractResponse> auditServiceContract;
    private List<DartNonAuditServiceContractResponse> nonAuditServiceContract;
    private List<DartOutsideDirectorChangeStatusResponse> outsideDirectorChangeStatus;
    private List<DartTotalStockStatusResponse> totalStockStatus;
    private List<DartTreasuryStockStatusResponse> treasuryStockStatus;
    private List<DartSingleCompanyKeyAccountResponse> singleCompanyKeyAccount;
    private List<DartNonConsolidatedFinancialStatementResponse> nonConsolidatedFinancialStatement;
    private List<DartCorporateBondBalanceResponse> corporateBondBalance;
    private List<DartCommercialPaperBalanceResponse> commercialPaperBalance;
    private List<DartShortTermBondBalanceResponse> shortTermBondBalance;
    private List<DartHybridSecuritiesBalanceResponse> hybridSecuritiesBalance;
    private List<DartMinorityShareholderStatusResponse> minorityShareholderStatus;
    private List<DartCompensationApprovalResponse> compensationApproval;
    private List<DartDirectorAndAuditorCompensationResponse> directorAndAuditorCompensation;
}