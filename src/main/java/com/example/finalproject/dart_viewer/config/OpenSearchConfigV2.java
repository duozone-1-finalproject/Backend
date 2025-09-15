package com.example.finalproject.dart_viewer.config;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OpenSearchConfigV2 {

    @Value("${spring.opensearch.uris}")
    private String openSearchUri;

    // Low-level RestClient를 별도 빈으로 등록 (종료 시 close)
    @Bean(name = "osRestClientV2", destroyMethod = "close")
    public RestClient restClientV2() {
<<<<<<< HEAD
        // 포트 없이 HttpHost 생성
        HttpHost targetHost = HttpHost.create(openSearchUri);
        return RestClient.builder(targetHost)
=======
        return RestClient.builder(new HttpHost("192.168.0.77", 9200, "http"))
                // 필요시 타임아웃/프록시 등 설정
                //.setRequestConfigCallback(b -> b.setConnectTimeout(5000).setSocketTimeout(60000))
>>>>>>> 137fbe8 (작업 임시 저장)
                .build();
    }

    // Transport도 별도 빈으로 등록 (종료 시 close)
    @Bean(name = "osTransportV2", destroyMethod = "close")
    public RestClientTransport restClientTransportV2(RestClient osRestClientV2) {
        return new RestClientTransport(osRestClientV2, new JacksonJsonpMapper());
    }

    // 기본 주입 대상 OpenSearchClient
    @Primary
    @Bean(name = "openSearchClientV2")
    public OpenSearchClient openSearchClientV2(RestClientTransport osTransportV2) {
        return new OpenSearchClient(osTransportV2);
    }
}
