package com.example.finalproject.ai_backend.service;

import com.example.finalproject.ai_backend.dto.AiRequestDto;
import com.example.finalproject.ai_backend.dto.AiResponseDto;
import com.example.finalproject.ai_backend.dto.CompanyDataDto2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportGenerationService {

    private final CompanyDataService2 companyDataService;
    private final TemplateService templateService;
    private final AiCommunicationService aiCommunicationService;
    private final OpenSearchService openSearchService;

    @Value("${ai.system.enabled:false}")
    private boolean aiSystemEnabled;

    /**
     * 보고서 생성 프로세스의 메인 메서드
     * 1. 회사 데이터 조회
     * 2. 템플릿에 변수 적용
     * 3. AI에게 Kafka로 요청 전송
     * 4. AI 응답 받기
     * 5. OpenSearch에 저장
     */
    public CompletableFuture<AiResponseDto> generateReport(String corpCode, String reportType) {
        String requestId = generateRequestId();
        log.info("보고서 생성 프로세스 시작: requestId={}, corpCode={}, reportType={}",
                requestId, corpCode, reportType);

        return companyDataService.getCompanyData(corpCode)
                .thenCompose(companyData -> {
                    log.info("회사 데이터 조회 완료: {}", companyData.getCorpName());
                    return processReportGeneration(requestId, companyData, reportType);
                })
                .thenCompose(aiResponse -> {
                    log.info("AI 보고서 생성 완료: {}", aiResponse.getRequestId());
                    // OpenSearch 저장은 회사 데이터를 다시 조회하지 않고 현재 데이터 사용
                    return saveToOpenSearch(aiResponse, corpCode);
                })
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("보고서 생성 프로세스 실패: {}", requestId, throwable);
                    } else {
                        log.info("보고서 생성 프로세스 완료: {}", requestId);
                    }
                });
    }

    /**
     * 회사 데이터와 템플릿으로 AI에게 Kafka 요청 전송
     */
    private CompletableFuture<AiResponseDto> processReportGeneration(String requestId, CompanyDataDto2 companyData, String reportType) {
        try {
            // 템플릿 가져오기
            String template = getTemplateByReportType(reportType);

            // 템플릿 변수 생성
            Map<String, String> variables = templateService.createTemplateVariables(companyData);

            // 변수가 적용된 템플릿 생성 (AI가 참고할 수 있도록)
            String processedTemplate = templateService.applyVariablesToTemplate(template, variables);

            // AI 요청 DTO 생성
            AiRequestDto aiRequest = AiRequestDto.builder()
                    .requestId(requestId)
                    .template(processedTemplate)
                    .companyData(companyData)
                    .build();

            log.info("AI 요청 준비 완료: {}, template length: {}", requestId, processedTemplate.length());

            // AiCommunicationService의 requestReportGeneration 메서드 사용
            // 이 메서드는 내부적으로 mock 모드 여부를 확인하여 처리
            log.info("AI 시스템을 통한 보고서 생성 요청: {} (AI시스템 활성화: {})", requestId, aiSystemEnabled);
            return aiCommunicationService.requestReportGeneration(aiRequest);

        } catch (Exception e) {
            log.error("보고서 생성 요청 처리 실패: {}", requestId, e);
            CompletableFuture<AiResponseDto> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    /**
     * AI 응답을 OpenSearch에 저장
     */
    private CompletableFuture<AiResponseDto> saveToOpenSearch(AiResponseDto aiResponse, String corpCode) {
        return companyDataService.getCompanyData(corpCode)
                .thenCompose(companyData -> {
                    log.info("OpenSearch 저장 시작: report={}, company={}",
                            aiResponse.getRequestId(), companyData.getCorpName());

                    return openSearchService.saveGeneratedReport(aiResponse, companyData)
                            .thenCompose(documentId -> {
                                log.info("보고서 저장 완료: {}", documentId);
                                return openSearchService.saveCompanyData(companyData);
                            })
                            .thenApply(companyDocumentId -> {
                                log.info("OpenSearch 저장 완료: report={}, company={}",
                                        aiResponse.getRequestId(), companyDocumentId);
                                return aiResponse;
                            });
                });
    }

    /**
     * 보고서 타입에 따른 템플릿 선택
     */
    private String getTemplateByReportType(String reportType) {
        log.info("보고서 타입별 템플릿 선택: {}", reportType);

        return switch (reportType.toLowerCase()) {
            case "증권신고서", "securities_registration" -> {
                log.info("증권신고서 템플릿 선택");
                yield templateService.getSecuritiesRegistrationTemplate();
            }
            case "사업보고서", "business_report" -> {
                log.info("사업보고서 템플릿 선택 (현재는 증권신고서 템플릿 사용)");
                // 추후 사업보고서 전용 템플릿 추가 예정
                yield templateService.getSecuritiesRegistrationTemplate();
            }
            case "분기보고서", "quarterly_report" -> {
                log.info("분기보고서 템플릿 선택 (현재는 증권신고서 템플릿 사용)");
                // 추후 분기보고서 전용 템플릿 추가 예정
                yield templateService.getSecuritiesRegistrationTemplate();
            }
            default -> {
                log.warn("알 수 없는 보고서 타입, 기본 템플릿 사용: {}", reportType);
                yield templateService.getSecuritiesRegistrationTemplate();
            }
        };
    }

    /**
     * 요청 ID 생성
     */
    private String generateRequestId() {
        String requestId = "REP_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        log.debug("새 요청 ID 생성: {}", requestId);
        return requestId;
    }

    /**
     * 보고서 생성 상태 확인
     */
    public CompletableFuture<String> getReportStatus(String requestId) {
        log.info("보고서 상태 확인: {}", requestId);

        return CompletableFuture.supplyAsync(() -> {
            // 대기 중인 요청 확인
            int pendingCount = aiCommunicationService.getPendingRequestCount();
            log.info("현재 대기 중인 요청 수: {}", pendingCount);

            // 실제 구현시 OpenSearch에서 상태 조회
            // 현재는 간단한 상태 반환
            return "PROCESSING";
        });
    }

    /**
     * 요청 취소
     */
    public CompletableFuture<Boolean> cancelReport(String requestId) {
        log.info("보고서 생성 요청 취소: {}", requestId);

        return CompletableFuture.supplyAsync(() -> {
            boolean cancelled = aiCommunicationService.cancelRequest(requestId);
            if (cancelled) {
                log.info("보고서 생성 요청 취소 성공: {}", requestId);
            } else {
                log.warn("보고서 생성 요청 취소 실패 (이미 완료되었거나 존재하지 않음): {}", requestId);
            }
            return cancelled;
        });
    }

    /**
     * 회사별 보고서 목록 조회
     */
    public CompletableFuture<Object> getReportsByCompany(String companyName) {
        log.info("회사별 보고서 조회: {}", companyName);

        return openSearchService.searchReportsByCompany(companyName)
                .thenApply(searchResponse -> {
                    long totalHits = searchResponse.getHits().getTotalHits().value;
                    log.info("조회된 보고서 수: {}", totalHits);

                    if (totalHits == 0) {
                        log.warn("해당 회사의 보고서가 존재하지 않음: {}", companyName);
                    }

                    return searchResponse.getHits();
                });
    }

    /**
     * 시스템 상태 확인
     */
    public CompletableFuture<Map<String, Object>> getSystemStatus() {
        log.info("시스템 상태 확인");

        return aiCommunicationService.checkKafkaConnection()
                .thenApply(kafkaStatus -> {
                    Map<String, Object> status = Map.of(
                            "kafka_connection", kafkaStatus,
                            "ai_system_enabled", aiSystemEnabled,
                            "pending_requests", aiCommunicationService.getPendingRequestCount(),
                            "timestamp", java.time.LocalDateTime.now().toString()
                    );

                    log.info("시스템 상태: {}", status);
                    return status;
                });
    }
}