package com.example.finalproject.ai_backend.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    // Kafka Topics - 환경변수에서 주입받도록 변경
    @Value("${kafka.topics.ai-request:ai-report-request}")
    public String AI_REQUEST_TOPIC;

    @Value("${kafka.topics.ai-response:ai-report-response}")
    public String AI_RESPONSE_TOPIC;

    @Value("${kafka.topics.fastapi-request:fastapi-equity-request}")
    public String FASTAPI_REQUEST_TOPIC;

    @Value("${kafka.topics.fastapi-response:fastapi-equity-response}")
    public String FASTAPI_RESPONSE_TOPIC;

    // 추가된 Validation 및 Revision 토픽들
    @Value("${kafka.topics.validation-request:ai-validation-request}")
    public String VALIDATION_REQUEST_TOPIC;

    @Value("${kafka.topics.validation-response:ai-validation-response}")
    public String VALIDATION_RESPONSE_TOPIC;

    @Value("${kafka.topics.revision-request:ai-revision-request}")
    public String REVISION_REQUEST_TOPIC;

    @Value("${kafka.topics.revision-response:ai-revision-response}")
    public String REVISION_RESPONSE_TOPIC;

    // OpenSearch Indices
    @Value("${opensearch.indices.report:ai-generated-reports}")
    public String REPORT_INDEX;

    @Value("${opensearch.indices.company:company-data}")
    public String COMPANY_INDEX;

    // Report Types - 이건 고정값으로 유지
    public static final String SECURITIES_REGISTRATION = "증권신고서";
    public static final String BUSINESS_REPORT = "사업보고서";
    public static final String QUARTERLY_REPORT = "분기보고서";

    // Status Codes
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_FAILED = "FAILED";

    // Response Messages
    public static final String MSG_SUCCESS = "요청이 성공적으로 처리되었습니다.";
    public static final String MSG_PROCESSING = "요청이 처리 중입니다.";
    public static final String MSG_FAILED = "요청 처리에 실패했습니다.";
}