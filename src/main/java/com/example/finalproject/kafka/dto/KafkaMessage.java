package com.example.finalproject.kafka.dto;

public class KafkaMessage<T> {
    private String requestId;
    private T data;
    // Constructors, Getters, and Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}