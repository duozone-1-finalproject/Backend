package com.example.finalproject.apitest.service.impl;

import com.example.finalproject.apitest.dto.common.AllDartDataResponse;
import com.example.finalproject.apitest.dto.equity.response.DartEquitySecuritiesGroupResponse;
import com.example.finalproject.apitest.dto.equity.response.DartEquitySecuritiesResponse;
import com.example.finalproject.apitest.dto.material.response.DartBwIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCbIssuanceResponse;
import com.example.finalproject.apitest.dto.material.response.DartCocoBondIssuanceResponse;
import com.example.finalproject.apitest.dto.overview.response.DartCompanyOverviewResponse;
import com.example.finalproject.apitest.dto.periodic.response.*;
import com.example.finalproject.apitest.exception.DartApiException;
import com.example.finalproject.apitest.service.TestService;
import com.example.finalproject.apitest.service.equity.DartEquitySecuritiesService;
import com.example.finalproject.apitest.service.material.DartBwIssuanceService;
import com.example.finalproject.apitest.service.material.DartCbIssuanceService;
import com.example.finalproject.apitest.service.material.DartCocoBondIssuanceService;
import com.example.finalproject.apitest.service.overview.DartCompanyOverviewService;
import com.example.finalproject.apitest.service.periodic.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    // --- 모든 개별 서비스 주입 ---
    private final DartCompanyOverviewService dartCompanyOverviewService;
    private final DartMajorShareholderStatusService dartMajorShareholderStatusService;
    private final DartMajorShareholderChangeService dartMajorShareholderChangeService;
    private final DartExecutiveStatusService dartExecutiveStatusService;
    private final DartEmployeeStatusService dartEmployeeStatusService;
    private final DartUnregisteredExecutiveCompensationService dartUnregisteredExecutiveCompensationService;
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
    private final DartEquitySecuritiesService dartEquitySecuritiesService;

    @Qualifier("taskExecutor")
    private final Executor taskExecutor;

    // --- 비동기 통합 메소드 구현 ---
    @Override
    public AllDartDataResponse fetchAllDartData(String corpCode, String bsnsYear, String reprtCode, String beginDate, String endDate, String fsDiv) {
        log.info("corpCode {}에 대한 모든 데이터 비동기 호출 시작", corpCode);

        // --- 각 API 호출을 비동기 작업으로 정의 ---
        CompletableFuture<DartCompanyOverviewResponse> companyOverviewFuture = supplyAsync(() -> DartCompanyOverviewCall(corpCode));
        CompletableFuture<List<DartMajorShareholderStatusResponse>> majorShareholderStatusFuture = supplyAsyncList(() -> DartMajorShareholderStatusCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartMajorShareholderChangeResponse>> majorShareholderChangeFuture = supplyAsyncList(() -> DartMajorShareholderChangeCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartExecutiveStatusResponse>> executiveStatusFuture = supplyAsyncList(() -> DartExecutiveStatusCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartEmployeeStatusResponse>> employeeStatusFuture = supplyAsyncList(() -> DartEmployeeStatusCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartUnregisteredExecutiveCompensationResponse>> unregisteredExecutiveCompensationFuture = supplyAsyncList(() -> DartUnregisteredExecutiveCompensationCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartCbIssuanceResponse>> cbIssuanceFuture = supplyAsyncList(() -> DartCbIssuanceCall(corpCode, beginDate, endDate));
        CompletableFuture<List<DartBwIssuanceResponse>> bwIssuanceFuture = supplyAsyncList(() -> DartBwIssuanceCall(corpCode, beginDate, endDate));
        CompletableFuture<List<DartCocoBondIssuanceResponse>> cocoBondIssuanceFuture = supplyAsyncList(() -> DartCocoBondIssuanceCall(corpCode, beginDate, endDate));
        CompletableFuture<List<DartPublicOfferingFundUsageResponse>> publicOfferingFundUsageFuture = supplyAsyncList(() -> DartPublicOfferingFundUsageCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartPrivatePlacementFundUsageResponse>> privatePlacementFundUsageFuture = supplyAsyncList(() -> DartPrivatePlacementFundUsageCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartAuditOpinionResponse>> auditOpinionFuture = supplyAsyncList(() -> DartAuditOpinionCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartAuditServiceContractResponse>> auditServiceContractFuture = supplyAsyncList(() -> DartAuditServiceContractCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartNonAuditServiceContractResponse>> nonAuditServiceContractFuture = supplyAsyncList(() -> DartNonAuditServiceContractCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartOutsideDirectorChangeStatusResponse>> outsideDirectorChangeStatusFuture = supplyAsyncList(() -> DartOutsideDirectorChangeStatusCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartTotalStockStatusResponse>> totalStockStatusFuture = supplyAsyncList(() -> DartTotalStockStatusCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartTreasuryStockStatusResponse>> treasuryStockStatusFuture = supplyAsyncList(() -> DartTreasuryStockStatusCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartSingleCompanyKeyAccountResponse>> singleCompanyKeyAccountFuture = supplyAsyncList(() -> DartSingleCompanyKeyAccountCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartNonConsolidatedFinancialStatementResponse>> nonConsolidatedFinancialStatementFuture = supplyAsyncList(() -> DartNonConsolidatedFinancialStatementCall(corpCode, bsnsYear, reprtCode, fsDiv));
        CompletableFuture<List<DartCorporateBondBalanceResponse>> corporateBondBalanceFuture = supplyAsyncList(() -> DartCorporateBondBalanceCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartCommercialPaperBalanceResponse>> commercialPaperBalanceFuture = supplyAsyncList(() -> DartCommercialPaperBalanceCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartShortTermBondBalanceResponse>> shortTermBondBalanceFuture = supplyAsyncList(() -> DartShortTermBondBalanceCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartHybridSecuritiesBalanceResponse>> hybridSecuritiesBalanceFuture = supplyAsyncList(() -> DartHybridSecuritiesBalanceCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartMinorityShareholderStatusResponse>> minorityShareholderStatusFuture = supplyAsyncList(() -> DartMinorityShareholderStatusCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartCompensationApprovalResponse>> compensationApprovalFuture = supplyAsyncList(() -> DartCompensationApprovalCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<List<DartDirectorAndAuditorCompensationResponse>> directorAndAuditorCompensationFuture = supplyAsyncList(() -> DartDirectorAndAuditorCompensationCall(corpCode, bsnsYear, reprtCode));
        CompletableFuture<DartEquitySecuritiesGroupResponse> equitySecuritiesFuture = supplyAsync(() -> DartEquitySecuritiesCall(corpCode, beginDate, endDate));

        // --- 정의된 모든 비동기 작업이 완료될 때까지 기다림 ---
        List<CompletableFuture<?>> allFutures = List.of(
                companyOverviewFuture, majorShareholderStatusFuture, majorShareholderChangeFuture, executiveStatusFuture, employeeStatusFuture,
                unregisteredExecutiveCompensationFuture, cbIssuanceFuture, bwIssuanceFuture, cocoBondIssuanceFuture, publicOfferingFundUsageFuture,
                privatePlacementFundUsageFuture, auditOpinionFuture, auditServiceContractFuture, nonAuditServiceContractFuture,
                outsideDirectorChangeStatusFuture, totalStockStatusFuture, treasuryStockStatusFuture, singleCompanyKeyAccountFuture,
                nonConsolidatedFinancialStatementFuture, corporateBondBalanceFuture, commercialPaperBalanceFuture, shortTermBondBalanceFuture,
                hybridSecuritiesBalanceFuture, minorityShareholderStatusFuture, compensationApprovalFuture, directorAndAuditorCompensationFuture,
                equitySecuritiesFuture
        );
        CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();

        // --- 모든 작업 완료 후, 결과를 취합하여 하나의 DTO로 만들어 반환 ---
        try {
            // [수정] AllDartDataResponse의 필드명에 맞게 builder 메소드명 수정
            return AllDartDataResponse.builder()
                    .companyOverview(companyOverviewFuture.get())
                    .majorShareholderStatus(majorShareholderStatusFuture.get())
                    .majorShareholderChange(majorShareholderChangeFuture.get())
                    .executiveStatus(executiveStatusFuture.get())
                    .employeeStatus(employeeStatusFuture.get())
                    .unregisteredExecutiveCompensation(unregisteredExecutiveCompensationFuture.get())
                    // .individualCompensations(individualCompensationFuture.get()) // DTO에 없는 필드
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
                    // .equitySecurities(equitySecuritiesFuture.get()) // DTO에 없는 필드
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            log.error("비동기 DART API 데이터 취합 중 최종 오류 발생", e);
            throw new DartApiException("데이터 취합 중 최종 오류가 발생했습니다.");
        }
    }

    // --- 비동기 호출 헬퍼 메소드 ---
    @FunctionalInterface
    private interface IOExceptionSupplier<T> {
        T get() throws IOException;
    }

    private <T> CompletableFuture<T> supplyAsync(IOExceptionSupplier<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.get();
            } catch (Exception e) {
                log.warn("비동기 API 호출 실패 (결과 null 반환): {}", e.getMessage());
                return null;
            }
        }, taskExecutor);
    }

    private <T> CompletableFuture<List<T>> supplyAsyncList(IOExceptionSupplier<List<T>> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.get();
            } catch (Exception e) {
                log.warn("비동기 API 호출 실패 (결과 empty list 반환): {}", e.getMessage());
                return Collections.emptyList();
            }
        }, taskExecutor);
    }

    // --- 개별 호출 메소드 구현 ---
    @Override
    public DartEquitySecuritiesGroupResponse DartEquitySecuritiesCall(String corpCode, String bgnDe, String endDe) throws IOException {
        return dartEquitySecuritiesService.dartEquitySecuritiesCall(corpCode, bgnDe, endDe);
    }

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
}

