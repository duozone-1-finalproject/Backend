package com.example.finalproject.dart.exception;

public class BusinessReportException extends Exception {
    public BusinessReportException(String message) {
        super(message);
    }

    public BusinessReportException(String message, Throwable cause) {
        super(message, cause);
    }
}