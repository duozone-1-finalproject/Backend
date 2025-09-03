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

    @Value("${opensearch.uris:http://192.168.0.77:9200}")
    private String openSearchUri;

    @Bean(name = "openSearchClient")
    public RestHighLevelClient openSearchClient() {
        try {
            // URI parsing
            String cleanUri = openSearchUri.replace("http://", "").replace("https://", "");
            String[] hostAndPort = cleanUri.split(":");
            String host = hostAndPort[0];
            int port = hostAndPort.length > 1 ? Integer.parseInt(hostAndPort[1]) : 9200;

            log.info("OpenSearch connection setup: {}:{}", host, port);

            // Create RestClientBuilder first
            RestClientBuilder builder = RestClient.builder(
                    new HttpHost(host, port, "http")
            ).setRequestConfigCallback(requestConfigBuilder ->
                    requestConfigBuilder
                            .setConnectTimeout(10000)  // 10 seconds
                            .setSocketTimeout(60000)   // 60 seconds
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
}