package com.example.finalproject.ai_backend.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OpenSearchConfig {

    // 환경변수로 변경 (기본값 추가)
    @Value("${spring.opensearch.uris:http://localhost:9200}")
    private String openSearchUri;

    @Value("${spring.opensearch.connection-timeout:5s}")
    private String connectionTimeout;

    @Value("${spring.opensearch.socket-timeout:60s}")
    private String socketTimeout;

    @Bean(name = "openSearchClient")
    public RestHighLevelClient openSearchClient() {
        try {
            // 쉼표로 구분된 URI들을 그대로 사용 (포트 기본값 지정/강제 없음)
            // 예: https://<ngrok-subdomain>.ngrok-free.app 또는 http://host:port
            String[] uriTokens = openSearchUri.split(",");
            HttpHost[] hosts = java.util.Arrays.stream(uriTokens)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(HttpHost::create) // 스킴/호스트/포트를 URI에서 그대로 해석
                    .toArray(HttpHost[]::new);

            log.info("OpenSearch connection setup with URIs: {}", openSearchUri);

            // 타임아웃 값 파싱 (예: "10s" -> 10000ms)
            int connectTimeoutMs = parseTimeoutToMillis(connectionTimeout, 10000);
            int socketTimeoutMs = parseTimeoutToMillis(socketTimeout, 60000);

            // RestClientBuilder 생성
            RestClientBuilder builder = RestClient.builder(hosts)
                    .setRequestConfigCallback(requestConfigBuilder ->
                            requestConfigBuilder
                                    .setConnectTimeout(connectTimeoutMs)
                                    .setSocketTimeout(socketTimeoutMs)
                    );

            // RestHighLevelClient 생성
            RestHighLevelClient client = new RestHighLevelClient(builder);

            // 연결 테스트
            try {
                var info = client.info(org.opensearch.client.RequestOptions.DEFAULT);
                log.info("OpenSearch connection successful: cluster={}, version={}",
                        info.getClusterName(), info.getVersion().getNumber());
            } catch (Exception e) {
                log.error("OpenSearch connection test failed: {}", e.getMessage());
                // 실패하더라도 빈은 생성 (실행 중 재시도 가능)
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