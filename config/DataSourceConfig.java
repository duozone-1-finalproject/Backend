package com.example.Backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    /**
     * 'prod' 프로필 전용 DataSource 설정입니다.
     * EKS와 같은 실제 운영 환경에서 활성화됩니다.
     */
    @Profile("prod")
    @Configuration
    static class ProductionDataSourceConfig {

        @Value("${DB_URL}")
        private String dbUrl;

        @Value("${DB_USERNAME}")
        private String dbUsername;

        @Value("${DB_SECRET_NAME}")
        private String secretName;

        // SecretsManagerService를 Spring Bean으로 등록
        @Bean
        public SecretsManagerService secretsManagerService() {
            return new SecretsManagerService();
        }

        @Bean
        public DataSource dataSource(SecretsManagerService secretsService) {
            // Secrets Manager에서 비밀번호 가져오기
            String dbPassword = secretsService.getSecret(secretName);

            return DataSourceBuilder.create()
                    .url(dbUrl)
                    .username(dbUsername)
                    .password(dbPassword)
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .build();
        }
    }
}
