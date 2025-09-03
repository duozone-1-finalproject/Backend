package com.example.finalproject.dart_viewer.config;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OpenSearchConfigV2 {

    // Low-level RestClient를 별도 빈으로 등록 (종료 시 close)
    @Bean(name = "osRestClientV2", destroyMethod = "close")
    public RestClient restClientV2() {
        return RestClient.builder(new HttpHost("localhost", 9200, "http"))
                // 필요시 타임아웃/프록시 등 설정
                //.setRequestConfigCallback(b -> b.setConnectTimeout(5000).setSocketTimeout(60000))
                .build();
    }

    // Transport도 별도 빈으로 등록 (종료 시 close)
    @Bean(name = "osTransportV2", destroyMethod = "close")
    public RestClientTransport restClientTransportV2(RestClient osRestClientV2) {
        return new RestClientTransport(osRestClientV2, new JacksonJsonpMapper());
    }

    // ★ 이름을 바꾸고(=중복 제거) 기본 주입 대상으로 지정
    @Primary
    @Bean(name = "openSearchClientV2")
    public OpenSearchClient openSearchClientV2(RestClientTransport osTransportV2) {
        return new OpenSearchClient(osTransportV2);
    }
}