package com.example.finalproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(
    // Specify the package for your Elasticsearch/OpenSearch repositories.
    basePackages = "com.example.finalproject.repository.opensearch"
)
public class ElasticsearchRepositoriesConfig {
}