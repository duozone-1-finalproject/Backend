package com.example.finalproject.apitest.dto.equity.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DartCompanyResponse {
    private String status;
    private String message;

    @JsonProperty("corp_code")
    private String corpCode;

    @JsonProperty("adres")
    private String adres;

    @JsonProperty("phn_no")
    private String phnNo;

    @JsonProperty("hm_url")
    private String hmUrl;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public String getCorpCode() { return corpCode; }
    public String getAdres() { return adres; }
    public String getPhnNo() { return phnNo; }
    public String getHmUrl() { return hmUrl; }

    public void setStatus(String status) { this.status = status; }
    public void setMessage(String message) { this.message = message; }
    public void setCorpCode(String corpCode) { this.corpCode = corpCode; }
    public void setAdres(String adres) { this.adres = adres; }
    public void setPhnNo(String phnNo) { this.phnNo = phnNo; }
    public void setHmUrl(String hmUrl) { this.hmUrl = hmUrl; }
}
