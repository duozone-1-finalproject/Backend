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

    // 환경변수로 변경
    @Value("${spring.opensearch.uris}")
    private String openSearchUri;

    @Value("${spring.opensearch.connection-timeout}")
    private String connectionTimeout;

    @Value("${spring.opensearch.socket-timeout}")
    private String socketTimeout;

    @Bean(name = "openSearchClient")
    public RestHighLevelClient openSearchClient() {
        try {
            // URI parsing - http:// 또는 https:// 제거
            String cleanUri = openSearchUri.replace("http://", "").replace("https://", "");
            String[] hostAndPort = cleanUri.split(":");
            String host = hostAndPort[0];
            int port = hostAndPort.length > 1 ? Integer.parseInt(hostAndPort[1]) : 9200;

            // HTTPS 지원을 위한 스키마 감지
            String scheme = openSearchUri.startsWith("https://") ? "https" : "http";

            log.info("OpenSearch connection setup: {}://{}:{}", scheme, host, port);

            // 타임아웃 값 파싱 (예: "10s" -> 10000ms)
            int connectTimeoutMs = parseTimeoutToMillis(connectionTimeout, 10000);
            int socketTimeoutMs = parseTimeoutToMillis(socketTimeout, 60000);

            // Create RestClientBuilder first
            RestClientBuilder builder = RestClient.builder(
                    new HttpHost(host, port, scheme)
            ).setRequestConfigCallback(requestConfigBuilder ->
                    requestConfigBuilder
                            .setConnectTimeout(connectTimeoutMs)
                            .setSocketTimeout(socketTimeoutMs)
            );

            // Create RestHighLevelClient using the builder
            RestHighLevelClient client = new RestHighLevelClient(builder);

            // Connection test
            try {
                var info = client.info(org.opensearch.client.RequestOptions.DEFAULT);
                log.info("OpenSearch connection successful: cluster={}, version={}",
                        info.getClusterName(), info.getVersion().getNumber());
            } catch (Exception e) {
                log.error("OpenSearch connection test failed: {}", e.getMessage());
                // Even if connection fails, create the bean (for runtime retry)
                log.warn("OpenSearch connection failed but client will be created. Runtime retry possible.");
            }

            return client;

        } catch (Exception e) {
            log.error("OpenSearch client setup failed: {}", e.getMessage(), e);
            throw new RuntimeException("Cannot initialize OpenSearch client: " + e.getMessage(), e);
        }
    }

    /**
     * 타임아웃 문자열을 밀리초로 변환 (예: "10s" -> 10000)
     */
    private int parseTimeoutToMillis(String timeout, int defaultValue) {
        try {
            if (timeout.endsWith("s")) {
                return Integer.parseInt(timeout.substring(0, timeout.length() - 1)) * 1000;
            } else if (timeout.endsWith("ms")) {
                return Integer.parseInt(timeout.substring(0, timeout.length() - 2));
            } else {
                return Integer.parseInt(timeout);
            }
        } catch (Exception e) {
            log.warn("타임아웃 파싱 실패, 기본값 사용: {} -> {}ms", timeout, defaultValue);
            return defaultValue;
        }
    }
}