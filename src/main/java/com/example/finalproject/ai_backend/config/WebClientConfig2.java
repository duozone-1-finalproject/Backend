// src/main/java/com/example/ai_backend/config/WebClientConfig.java
package com.example.finalproject.ai_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebClientConfig2 {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
                .defaultHeader("User-Agent", "AI-Backend/1.0")
                .defaultHeader("Accept", "application/json");
    }
}
