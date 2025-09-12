package com.example.finalproject.ai_backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.opensearch")
public class OpenSearchProperties {
    private List<String> uris;
    private String username;
    private String password;
    private Duration connectionTimeout = Duration.ofSeconds(10);
    private Duration socketTimeout = Duration.ofSeconds(60);
}