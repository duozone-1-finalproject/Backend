package com.example.backend.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    private final SecretsManagerService secretsService;
    private final String secretName = "rds/testuser"; // AWS Secrets 이름

    public DataSourceConfig(SecretsManagerService secretsService) {
        this.secretsService = secretsService;
    }

    @Bean
    public DataSource dataSource() {
        Map<String, String> secret = secretsService.getSecretMap(secretName);

        String url = String.format(
                "jdbc:mariadb://%s:%s/%s",
                secret.get("host"),
                secret.get("port"),
                secret.get("dbname")
        );

        return DataSourceBuilder.create()
                .url(url)
                .username(secret.get("username"))
                .password(secret.get("password"))
                .driverClassName("org.mariadb.jdbc.Driver")
                .build();
    }
}
