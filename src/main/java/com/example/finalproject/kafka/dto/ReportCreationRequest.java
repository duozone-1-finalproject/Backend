package com.example.finalproject.kafka.dto;

import java.util.Map;

public class ReportCreationRequest {
    private String companyName;
    private String reportType;
    private Map<String, Object> financialData;
    // Getters and Setters
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public Map<String, Object> getFinancialData() { return financialData; }
    public void setFinancialData(Map<String, Object> financialData) { this.financialData = financialData; }
}