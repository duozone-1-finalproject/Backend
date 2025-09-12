package com.example.finalproject.apitest.dto.equity.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DartEquitySecuritiesResponse {
    private List<EquityGeneralInfoResponse> generalInfos;
    private List<EquitySecurityTypeResponse> securityTypes;
    private List<EquityFundUsageResponse> fundUsages;
    private List<EquitySellerInfoResponse> sellerInfos;
    private List<EquityUnderwriterInfoResponse> underwriterInfos;
    private List<EquityRepurchaseOptionResponse> repurchaseOptions;
}
