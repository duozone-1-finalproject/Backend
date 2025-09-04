// src/main/java/com/example/ai_backend/common/Constants.java
package com.example.finalproject.ai_backend.common;

public final class Constants {

    // Kafka Topics
    public static final String AI_REQUEST_TOPIC = "ai-report-request";
    public static final String AI_RESPONSE_TOPIC = "ai-report-response";

    // OpenSearch Indices
    public static final String REPORT_INDEX = "ai-generated-reports";
    public static final String COMPANY_INDEX = "company-data";

    // Report Types
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

    private Constants() {
        // Utility class
    }
}