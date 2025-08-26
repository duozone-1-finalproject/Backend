package com.example.finalproject.apitest.exception;

public class DartApiException extends RuntimeException {
    public DartApiException(String message) {
        super(message);
    }

    public DartApiException(String message, Throwable cause) {
        super(message, cause);
    }
}