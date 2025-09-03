package com.example.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * 애플리케이션의 주요 Bean 설정과 외부 구성(Secrets)을 담당합니다.
 * Spring Cloud AWS를 통해 Secrets Manager의 값을 자동으로 주입받습니다.
 * 이 설정은 'test' 프로필이 아닐 때만 활성화됩니다.
 */
@Configuration
@EnableConfigurationProperties(AppSecrets.class)
@Profile("!test")
public class AppConfig {

    // Spring Cloud AWS가 'rds/testuser' 보안 암호에서 주입하는 값들
    @Value("${host}")
    private String host;
    @Value("${port}")
    private String port;
    @Value("${dbname}")
    private String dbname;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        String url = String.format("jdbc:mariadb://%s:%s/%s", host, port, dbname);
        return DataSourceBuilder.create()
                .url(url)
                .username(this.username)
                .password(this.password)
                .driverClassName("org.mariadb.jdbc.Driver")
                .build();
    }
}