package com.example.finalproject.ai_backend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Configuration
public class MariaDBConfig {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean(name = "mariaDataSource")
    public DataSource mariaDataSource() {
        try {
            log.info("MariaDB connection setup: url={}, user={}", jdbcUrl, username);

            // HikariCP 설정
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setDriverClassName(driverClassName);

            // 풀 최적화 (필요시 조정 가능)
            config.setMaximumPoolSize(20);
            config.setMinimumIdle(5);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            config.setPoolName("MariaDB-HikariCP");

            HikariDataSource dataSource = new HikariDataSource(config);

            // 연결 테스트
            try (Connection conn = dataSource.getConnection()) {
                log.info("MariaDB connection successful: product={}, version={}",
                        conn.getMetaData().getDatabaseProductName(),
                        conn.getMetaData().getDatabaseProductVersion());
            } catch (Exception e) {
                log.error("MariaDB connection test failed: {}", e.getMessage());
                log.warn("DataSource will still be created, but runtime errors may occur.");
            }

            return dataSource;

        } catch (Exception e) {
            log.error("MariaDB DataSource setup failed: {}", e.getMessage(), e);
            throw new RuntimeException("Cannot initialize MariaDB DataSource: " + e.getMessage(), e);
        }
    }
}
