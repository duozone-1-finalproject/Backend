package com.example.finalproject.ai_backend.config;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class OpenSearchConfig {

    @Value("${spring.opensearch.uris}")
    private String openSearchUri;

    @Bean(name = "openSearchClient")
    public RestHighLevelClient openSearchClient() {
        try {
            // HttpHost를 포트 없이 생성
            HttpHost targetHost = HttpHost.create(openSearchUri);

            log.info("OpenSearch connection setup: {}", targetHost);

            RestClientBuilder builder = RestClient.builder(targetHost)
                    .setRequestConfigCallback(requestConfigBuilder ->
                            requestConfigBuilder
                                    .setConnectTimeout(10000)  // 10초
                                    .setSocketTimeout(60000)   // 60초
                    );

            RestHighLevelClient client = new RestHighLevelClient(builder);

            // 연결 테스트
            try {
                var info = client.info(org.opensearch.client.RequestOptions.DEFAULT);
                log.info("OpenSearch connection successful: cluster={}, version={}",
                        info.getClusterName(), info.getVersion().getNumber());
            } catch (Exception e) {
                log.error("OpenSearch connection test failed: {}", e.getMessage());
                log.warn("Connection failed but client will be created. Runtime retry possible.");
            }

            return client;

        } catch (Exception e) {
            log.error("OpenSearch client setup failed: {}", e.getMessage(), e);
            throw new RuntimeException("Cannot initialize OpenSearch client: " + e.getMessage(), e);
        }
    }
}