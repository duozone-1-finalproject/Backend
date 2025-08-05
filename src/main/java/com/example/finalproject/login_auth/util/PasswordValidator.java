package com.example.finalproject.login_auth.util;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValid(String password) {
        return password != null && pattern.matcher(password).matches();
    }
}