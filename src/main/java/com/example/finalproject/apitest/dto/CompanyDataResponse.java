package com.example.finalproject.apitest.dto;
import com.example.finalproject.apitest.entity.equity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDataResponse {
    private String corpCode;
    private List<DartGeneralInfo> generalInfo;
    private List<DartSecuritiesInfo> securitiesInfo;
    private List<DartUnderwriterInfo> underwriterInfo;
    private List<DartFundUsage> fundUsage;
    private List<DartSellerInfo> sellerInfo;
    private List<DartRedemptionRights> redemptionRights; }