package com.example.finalproject.apitest.dto;

public class CompanyOverviewResponse {
    private Company company;

    public CompanyOverviewResponse() {}
    public CompanyOverviewResponse(Company company) { this.company = company; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
}
