package com.example.finalproject.login_auth.util;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomOAuth2User {
    private final String email;
    private final String name;

    public CustomOAuth2User(Map<String, Object> attributes) {
        this.email = (String) attributes.get("email");
        this.name = (String) attributes.get("name");
    }
}
