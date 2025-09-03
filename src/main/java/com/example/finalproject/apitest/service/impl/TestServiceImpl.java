package com.example.finalproject.apitest.service.impl;

import com.example.finalproject.apitest.dto.common.AllDartDataResponse;
import com.example.finalproject.apitest.dto.material.response.DartBwIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCbIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCocoBondIssuanceResponse;
import com.example.finalproject.apitest.dto.overview.response.DartCompanyOverviewResponse;
import com.example.finalproject.apitest.dto.periodic.response.*;
import com.example.finalproject.apitest.exception.DartApiException;
import com.example.finalproject.apitest.service.material.DartBwIssuanceService;
import com.example.finalproject.apitest.service.material.DartCbIssuanceService;
import com.example.finalproject.apitest.service.material.DartCocoBondIssuanceService;
import com.example.finalproject.apitest.service.overview.DartCompanyOverviewService;
import com.example.finalproject.apitest.service.periodic.*;
import com.example.finalproject.apitest.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.finalproject.apitest.service.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final DartCompanyOverviewService dartCompanyOverviewService;
    private final DartMajorShareholderStatusService dartMajorShareholderStatusService;
    private final DartMajorShareholderChangeService dartMajorShareholderChangeService;
    private final DartExecutiveStatusService dartExecutiveStatusService;
    private final DartEmployeeStatusService dartEmployeeStatusService;
    private final DartUnregisteredExecutiveCompensationService dartUnregisteredExecutiveCompensationService; // [추가]
    private final DartCbIssuanceService dartCbIssuanceService;
    private final DartBwIssuanceService dartBwIssuanceService;
    private final DartCocoBondIssuanceService dartCocoBondIssuanceService;
    private final DartPublicOfferingFundUsageService dartPublicOfferingFundUsageService;
    private final DartPrivatePlacementFundUsageService dartPrivatePlacementFundUsageService;
    private final DartAuditOpinionService dartAuditOpinionService;
    private final DartAuditServiceContractService dartAuditServiceContractService;
    private final DartNonAuditServiceContractService dartNonAuditServiceContractService;
    private final DartOutsideDirectorChangeStatusService dartOutsideDirectorChangeStatusService;
    private final DartTotalStockStatusService dartTotalStockStatusService;
    private final DartTreasuryStockStatusService dartTreasuryStockStatusService;
    private final DartSingleCompanyKeyAccountService dartSingleCompanyKeyAccountService;
    private final DartNonConsolidatedFinancialStatementService dartNonConsolidatedFinancialStatementService;
    private final DartCorporateBondBalanceService dartCorporateBondBalanceService;
    private final DartCommercialPaperBalanceService dartCommercialPaperBalanceService;
    private final DartShortTermBondBalanceService dartShortTermBondBalanceService;
    private final DartHybridSecuritiesBalanceService dartHybridSecuritiesBalanceService;
    private final DartMinorityShareholderStatusService dartMinorityShareholderStatusService;
    private final DartCompensationApprovalService dartCompensationApprovalService;
    private final DartDirectorAndAuditorCompensationService dartDirectorAndAuditorCompensationService;

    private final Executor taskExecutor;

    @Override
    public DartCompanyOverviewResponse DartCompanyOverviewCall(String corpCode) throws IOException {
        return dartCompanyOverviewService.dartCompanyOverviewCall(corpCode);
    }

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
    public List<DartEmployeeStatusResponse> DartEmployeeStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartEmployeeStatusService.dartEmployeeStatusCall(corpCode, bsnsYear, reprtCode);
    }

    // [추가]
    @Override
    public List<DartUnregisteredExecutiveCompensationResponse> DartUnregisteredExecutiveCompensationCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartUnregisteredExecutiveCompensationService.dartUnregisteredExecutiveCompensationCall(corpCode, bsnsYear, reprtCode);
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

    @Override
    public List<DartPrivatePlacementFundUsageResponse> DartPrivatePlacementFundUsageCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartPrivatePlacementFundUsageService.dartPrivatePlacementFundUsageCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartAuditOpinionResponse> DartAuditOpinionCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartAuditOpinionService.dartAuditOpinionCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartAuditServiceContractResponse> DartAuditServiceContractCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartAuditServiceContractService.dartAuditServiceContractCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartNonAuditServiceContractResponse> DartNonAuditServiceContractCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartNonAuditServiceContractService.dartNonAuditServiceContractCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartOutsideDirectorChangeStatusResponse> DartOutsideDirectorChangeStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartOutsideDirectorChangeStatusService.dartOutsideDirectorChangeStatusCall(corpCode, bsnsYear, reprtCode);
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

    @Override
    public List<DartMinorityShareholderStatusResponse> DartMinorityShareholderStatusCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartMinorityShareholderStatusService.dartMinorityShareholderStatusCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartCompensationApprovalResponse> DartCompensationApprovalCall(String corpCode, String bsnsYear, String reprtCode) throws IOException {
        return dartCompensationApprovalService.dartCompensationApprovalCall(corpCode, bsnsYear, reprtCode);
    }

    @Override
    public List<DartDirectorAndAuditorCompensationResponse> DartDirectorAndAuditorCompensationCall(String corpCode, String bsnsYear, String reprtCode) throws IOException{
        return dartDirectorAndAuditorCompensationService.dartDirectorAndAuditorCompensationCall(corpCode, bsnsYear, reprtCode);
    }

    // --- 비동기 통합 메소드 구현 ---
    @Override
    public AllDartDataResponse fetchAllDartData(String corpCode, String bsnsYear, String reprtCode, String beginDate, String endDate, String fsDiv) {
        log.info("corpCode {}에 대한 모든 데이터 비동기 호출 시작", corpCode);

        // 각 API 호출을 비동기 작업으로 정의하고, 실패 시 기본값을 반환하도록 .exceptionally()를 추가합니다.
        CompletableFuture<DartCompanyOverviewResponse> companyOverviewFuture = CompletableFuture.supplyAsync(() -> { try { return DartCompanyOverviewCall(corpCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("CompanyOverview API 호출 실패: {}", ex.getMessage()); return null; });
        CompletableFuture<List<DartMajorShareholderStatusResponse>> majorShareholderStatusFuture = CompletableFuture.supplyAsync(() -> { try { return DartMajorShareholderStatusCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("MajorShareholderStatus API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartMajorShareholderChangeResponse>> majorShareholderChangeFuture = CompletableFuture.supplyAsync(() -> { try { return DartMajorShareholderChangeCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("MajorShareholderChange API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartExecutiveStatusResponse>> executiveStatusFuture = CompletableFuture.supplyAsync(() -> { try { return DartExecutiveStatusCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("ExecutiveStatus API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartEmployeeStatusResponse>> employeeStatusFuture = CompletableFuture.supplyAsync(() -> { try { return DartEmployeeStatusCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("EmployeeStatus API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartUnregisteredExecutiveCompensationResponse>> unregisteredExecutiveCompensationFuture = CompletableFuture.supplyAsync(() -> { try { return DartUnregisteredExecutiveCompensationCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("UnregisteredExecutiveCompensation API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartCbIssuanceResponse>> cbIssuanceFuture = CompletableFuture.supplyAsync(() -> { try { return DartCbIssuanceCall(corpCode, beginDate, endDate); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("CbIssuance API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartBwIssuanceResponse>> bwIssuanceFuture = CompletableFuture.supplyAsync(() -> { try { return DartBwIssuanceCall(corpCode, beginDate, endDate); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("BwIssuance API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartCocoBondIssuanceResponse>> cocoBondIssuanceFuture = CompletableFuture.supplyAsync(() -> { try { return DartCocoBondIssuanceCall(corpCode, beginDate, endDate); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("CocoBondIssuance API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartPublicOfferingFundUsageResponse>> publicOfferingFundUsageFuture = CompletableFuture.supplyAsync(() -> { try { return DartPublicOfferingFundUsageCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("PublicOfferingFundUsage API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartPrivatePlacementFundUsageResponse>> privatePlacementFundUsageFuture = CompletableFuture.supplyAsync(() -> { try { return DartPrivatePlacementFundUsageCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("PrivatePlacementFundUsage API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartAuditOpinionResponse>> auditOpinionFuture = CompletableFuture.supplyAsync(() -> { try { return DartAuditOpinionCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("AuditOpinion API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartAuditServiceContractResponse>> auditServiceContractFuture = CompletableFuture.supplyAsync(() -> { try { return DartAuditServiceContractCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("AuditServiceContract API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartNonAuditServiceContractResponse>> nonAuditServiceContractFuture = CompletableFuture.supplyAsync(() -> { try { return DartNonAuditServiceContractCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("NonAuditServiceContract API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartOutsideDirectorChangeStatusResponse>> outsideDirectorChangeStatusFuture = CompletableFuture.supplyAsync(() -> { try { return DartOutsideDirectorChangeStatusCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("OutsideDirectorChangeStatus API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartTotalStockStatusResponse>> totalStockStatusFuture = CompletableFuture.supplyAsync(() -> { try { return DartTotalStockStatusCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("TotalStockStatus API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartTreasuryStockStatusResponse>> treasuryStockStatusFuture = CompletableFuture.supplyAsync(() -> { try { return DartTreasuryStockStatusCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("TreasuryStockStatus API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartSingleCompanyKeyAccountResponse>> singleCompanyKeyAccountFuture = CompletableFuture.supplyAsync(() -> { try { return DartSingleCompanyKeyAccountCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("SingleCompanyKeyAccount API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartNonConsolidatedFinancialStatementResponse>> nonConsolidatedFinancialStatementFuture = CompletableFuture.supplyAsync(() -> { try { return DartNonConsolidatedFinancialStatementCall(corpCode, bsnsYear, reprtCode, fsDiv); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("NonConsolidatedFinancialStatement API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartCorporateBondBalanceResponse>> corporateBondBalanceFuture = CompletableFuture.supplyAsync(() -> { try { return DartCorporateBondBalanceCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("CorporateBondBalance API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartCommercialPaperBalanceResponse>> commercialPaperBalanceFuture = CompletableFuture.supplyAsync(() -> { try { return DartCommercialPaperBalanceCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("CommercialPaperBalance API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartShortTermBondBalanceResponse>> shortTermBondBalanceFuture = CompletableFuture.supplyAsync(() -> { try { return DartShortTermBondBalanceCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("ShortTermBondBalance API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartHybridSecuritiesBalanceResponse>> hybridSecuritiesBalanceFuture = CompletableFuture.supplyAsync(() -> { try { return DartHybridSecuritiesBalanceCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("HybridSecuritiesBalance API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartMinorityShareholderStatusResponse>> minorityShareholderStatusFuture = CompletableFuture.supplyAsync(() -> { try { return DartMinorityShareholderStatusCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("MinorityShareholderStatus API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartCompensationApprovalResponse>> compensationApprovalFuture = CompletableFuture.supplyAsync(() -> { try { return DartCompensationApprovalCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("CompensationApproval API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });
        CompletableFuture<List<DartDirectorAndAuditorCompensationResponse>> directorAndAuditorCompensationFuture = CompletableFuture.supplyAsync(() -> { try { return DartDirectorAndAuditorCompensationCall(corpCode, bsnsYear, reprtCode); } catch (IOException e) { throw new RuntimeException(e); } }, taskExecutor).exceptionally(ex -> { log.warn("DirectorAndAuditorCompensation API 호출 실패: {}", ex.getMessage()); return Collections.emptyList(); });

        // 정의된 모든 비동기 작업이 완료될 때까지 기다립니다.
        List<CompletableFuture<?>> allFutures = List.of(
                companyOverviewFuture, majorShareholderStatusFuture, majorShareholderChangeFuture, executiveStatusFuture, employeeStatusFuture,
                unregisteredExecutiveCompensationFuture, cbIssuanceFuture, bwIssuanceFuture, cocoBondIssuanceFuture, publicOfferingFundUsageFuture,
                privatePlacementFundUsageFuture, auditOpinionFuture, auditServiceContractFuture, nonAuditServiceContractFuture,
                outsideDirectorChangeStatusFuture, totalStockStatusFuture, treasuryStockStatusFuture, singleCompanyKeyAccountFuture,
                nonConsolidatedFinancialStatementFuture, corporateBondBalanceFuture, commercialPaperBalanceFuture, shortTermBondBalanceFuture,
                hybridSecuritiesBalanceFuture, minorityShareholderStatusFuture, compensationApprovalFuture, directorAndAuditorCompensationFuture
        );
        CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();

        // 모든 작업이 완료되면, 결과를 취합하여 하나의 DTO로 만들어 반환합니다.
        try {
            return AllDartDataResponse.builder()
                    .companyOverview(companyOverviewFuture.get())
                    .majorShareholderStatus(majorShareholderStatusFuture.get())
                    .majorShareholderChange(majorShareholderChangeFuture.get())
                    .executiveStatus(executiveStatusFuture.get())
                    .employeeStatus(employeeStatusFuture.get())
                    .unregisteredExecutiveCompensation(unregisteredExecutiveCompensationFuture.get())
                    .cbIssuance(cbIssuanceFuture.get())
                    .bwIssuance(bwIssuanceFuture.get())
                    .cocoBondIssuance(cocoBondIssuanceFuture.get())
                    .publicOfferingFundUsage(publicOfferingFundUsageFuture.get())
                    .privatePlacementFundUsage(privatePlacementFundUsageFuture.get())
                    .auditOpinion(auditOpinionFuture.get())
                    .auditServiceContract(auditServiceContractFuture.get())
                    .nonAuditServiceContract(nonAuditServiceContractFuture.get())
                    .outsideDirectorChangeStatus(outsideDirectorChangeStatusFuture.get())
                    .totalStockStatus(totalStockStatusFuture.get())
                    .treasuryStockStatus(treasuryStockStatusFuture.get())
                    .singleCompanyKeyAccount(singleCompanyKeyAccountFuture.get())
                    .nonConsolidatedFinancialStatement(nonConsolidatedFinancialStatementFuture.get())
                    .corporateBondBalance(corporateBondBalanceFuture.get())
                    .commercialPaperBalance(commercialPaperBalanceFuture.get())
                    .shortTermBondBalance(shortTermBondBalanceFuture.get())
                    .hybridSecuritiesBalance(hybridSecuritiesBalanceFuture.get())
                    .minorityShareholderStatus(minorityShareholderStatusFuture.get())
                    .compensationApproval(compensationApprovalFuture.get())
                    .directorAndAuditorCompensation(directorAndAuditorCompensationFuture.get())
                    .build();
        } catch (Exception e) {
            log.error("비동기 DART API 데이터 취합 중 최종 오류 발생", e);
            throw new DartApiException("데이터 취합 중 최종 오류가 발생했습니다.");
        }
    }
}
