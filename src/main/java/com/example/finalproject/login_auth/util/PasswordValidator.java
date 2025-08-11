package com.example.finalproject.login_auth.util;

import java.util.regex.Pattern;

public class PasswordValidator {

    // 최소 8자, 숫자/대문자/소문자 각각 최소 1개 포함
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValid(String password) {
        return pattern.matcher(password).matches();
    }
}
