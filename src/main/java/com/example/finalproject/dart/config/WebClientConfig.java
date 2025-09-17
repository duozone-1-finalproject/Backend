package com.example.finalproject.dart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

// dart 오픈api 사용을 위한 WebClient

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


    private void customizeCodecs(ClientCodecConfigurer configurer) {
        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024); // 10MB 등으로 조절
    }
}
