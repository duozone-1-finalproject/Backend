package com.example.finalproject.login_auth.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {

    private String secretKey;
    private Duration expiration = Duration.ofHours(1); // 기본값 1시간
    private Duration refreshExpiration = Duration.ofDays(7); // 기본값 7일

}