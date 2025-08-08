package com.example.finalproject.kafka.dto;

public class ReportResult {
    private String generatedReportUrl;
    private String reportSummary;
    // Getters and Setters
    public String getGeneratedReportUrl() { return generatedReportUrl; }
    public void setGeneratedReportUrl(String generatedReportUrl) { this.generatedReportUrl = generatedReportUrl; }
    public String getReportSummary() { return reportSummary; }
    public void setReportSummary(String reportSummary) { this.reportSummary = reportSummary; }
}
