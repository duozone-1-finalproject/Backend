package com.example.finalproject.dart.config;

import org.apache.http.HttpHost;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfig {

    @Value("${spring.data.opensearch.url}")
    private String url;

    @Value("${spring.data.opensearch.port}")
    private String port;

    @Value("${spring.data.opensearch.protocol}")
    private String protocol;

    @Bean
    public OpenSearchClient openSearchClient() {
        // Low-level REST 클라이언트 생성
        RestClient restClient = RestClient.builder(
                new HttpHost(url, Integer.parseInt(port), protocol) // HTTP 연결 (SSL 사용 시 "https")
        ).build();

        // Transport 객체 생성
        RestClientTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );

        // OpenSearchClient 생성
        return new OpenSearchClient(transport);
    }
}
