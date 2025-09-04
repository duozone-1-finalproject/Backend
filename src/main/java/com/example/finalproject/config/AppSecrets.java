package com.example.finalproject.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties
public class AppSecrets {

    // ===== DB =====
    private String host;
    private String port;
    private String dbname;
    private String username;
    private String password;

    // ===== JWT =====
    private String jwtSecretKey;
    private String jwtExpiration;
    private String jwtRefreshExpiration;

    // ===== DART =====
    private String dartApiKey;
    private String dartApiKeyObj;
    private String dartApiKeyLjh;
    private String dartBaseUrl;

    // ===== OAuth2 =====
    private String kakaoClientId;
    private String kakaoClientSecret;
    private String naverClientId;
    private String naverClientSecret;
    private String googleClientId;
    private String googleClientSecret;

    // ===== Frontend URL =====
    private String frontendUrl;

    // ===== Kafka =====
    private String kafkaBootstrapServers;

    // ===== OpenSearch / Elasticsearch =====
    private String elasticsearchEndpoints;
}
