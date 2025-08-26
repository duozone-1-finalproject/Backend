package com.example.finalproject.apitest.service.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@Slf4j
public class Support {
    // 스트링 안전한 형변환기 -------------------------------------------------------------------------------------------
    // 안전하게 long 타입으로 변환
    public Long safeParseLong(String textValue) {
        if (!StringUtils.hasText(textValue) || "-".equals(textValue)) {
            return null;
        }
        try {
            return Long.parseLong(textValue.replace(",", ""));
        } catch (NumberFormatException e) {
            log.warn("Long 타입으로 변환할 수 없는 값입니다: {}", textValue);
            return null;
        }
    }

    // 안전하게 double 타입으로 변환
    public Double safeParseDouble(String textValue) {
        if (!StringUtils.hasText(textValue) || "-".equals(textValue)) {
            return null;
        }
        try {
            return Double.parseDouble(textValue.replace(",", ""));
        } catch (NumberFormatException e) {
            log.warn("Double 타입으로 변환할 수 없는 값입니다: {}", textValue);
            return null;
        }
    }

    // 안전하게 LocalDate 타입으로 변환
    public LocalDate safeParseLocalDate(String textValue) {
        if (!StringUtils.hasText(textValue)) {
            return null;
        }
        try {
            return LocalDate.parse(textValue);
        } catch (DateTimeParseException e) {
            log.warn("LocalDate 타입으로 변환할 수 없는 값입니다: {}", textValue);
            return null;
        }
    }

    public LocalDate safeParseLocalDate(String textValue, String pattern) {
        if (!StringUtils.hasText(textValue)) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(textValue, formatter);
        } catch (DateTimeParseException e) {
            log.warn("'{}' 패턴으로 변환할 수 없는 값입니다: {}", pattern, textValue);
            return null;
        }
    }

    public Integer safeParseInteger(String textValue) {
        if (!StringUtils.hasText(textValue) || "-".equals(textValue)) {
            return null;
        }
        try {
            return Integer.parseInt(textValue.replace(",", ""));
        } catch (NumberFormatException e) {
            log.warn("Integer 타입으로 변환할 수 없는 값입니다: {}", textValue);
            return null;
        }
    }
    // --------------------------------------------------------------------------------------------------------------
}

