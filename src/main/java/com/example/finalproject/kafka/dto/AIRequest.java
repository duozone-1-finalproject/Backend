package com.example.finalproject.kafka.dto;

public class AIRequest {
    private String userId;
    private String message;

    public AIRequest() {}

    public AIRequest(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
