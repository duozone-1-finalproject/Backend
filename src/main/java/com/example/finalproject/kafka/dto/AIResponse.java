package com.example.finalproject.kafka.dto;

public class AIResponse {
    private String responseMessage;

    public AIResponse() {}

    public AIResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() { return responseMessage; }
    public void setResponseMessage(String responseMessage) { this.responseMessage = responseMessage; }
}
