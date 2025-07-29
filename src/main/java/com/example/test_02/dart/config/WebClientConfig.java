package com.example.test_02.dart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient dartWebClient() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(this::customizeCodecs)
                .build();

        return WebClient.builder()
                .baseUrl("https://opendart.fss.or.kr")
                .exchangeStrategies(strategies)
                .build();
    }

    @Bean
    public WebClient myWebClient() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(this::customizeCodecs)
                .build();

        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .exchangeStrategies(strategies)
                .build();
    }


    private void customizeCodecs(ClientCodecConfigurer configurer) {
        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024); // 10MB 등으로 조절
    }
}
