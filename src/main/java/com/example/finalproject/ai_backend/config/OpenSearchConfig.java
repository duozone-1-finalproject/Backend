package com.example.finalproject.ai_backend.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.time.Duration;


@Configuration
@Slf4j
public class OpenSearchConfig {

    @Value("${spring.opensearch.uris}")
    private String serverUrl;

    @Value("${spring.opensearch.username}")
    private String username;

    @Value("${spring.opensearch.password}")
    private String password;

    @Value("${spring.opensearch.connection-timeout:10s}")
    private Duration connectionTimeout;

    @Value("${spring.opensearch.socket-timeout:60s}")
    private Duration socketTimeout;

    @Value("${spring.opensearch.max-conn-total:100}")
    private int maxConnTotal;

    @Value("${spring.opensearch.max-conn-per-route:100}")
    private int maxConnPerRoute;

    private RestClient restClient; // PreDestroy에서 참조할 수 있도록 필드로 변경

    @Bean
    @Primary // 프로젝트 내 유일한 OpenSearchClient Bean으로 지정
    public OpenSearchClient openSearchClient() {
        // 1. 인증 정보 설정
        final var credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        // 2. Low-level REST 클라이언트 생성 (인증, 타임아웃, 커넥션 풀 설정 포함)
        this.restClient = RestClient.builder(HttpHost.create(serverUrl))
                .setHttpClientConfigCallback(
                        httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                                .setMaxConnTotal(maxConnTotal)
                                .setMaxConnPerRoute(maxConnPerRoute)
                )
                .setRequestConfigCallback(
                        requestConfigBuilder -> requestConfigBuilder
                                .setConnectTimeout((int) connectionTimeout.toMillis())
                                .setSocketTimeout((int) socketTimeout.toMillis())
                )
                .build();

        // 3. Transport 객체 생성
        var transport = new RestClientTransport(this.restClient, new JacksonJsonpMapper());
        
        // 4. 최종 OpenSearchClient 반환
        return new OpenSearchClient(transport);
    }

    /**
     * 애플리케이션 종료 시 OpenSearch RestClient 리소스를 안전하게 닫습니다.
     */
    @PreDestroy
    public void closeRestClient() {
        if (this.restClient != null) {
            try {
                log.info("Closing OpenSearch RestClient...");
                this.restClient.close();
                log.info("OpenSearch RestClient closed successfully.");
            } catch (IOException e) {
                log.error("Failed to close OpenSearch RestClient", e);
            }
        }
    }
}
