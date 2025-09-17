package com.example.finalproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
    // Specify the package where your JPA repositories are located.
    basePackages = "com.example.finalproject.repository.jpa"
)
public class JpaRepositoriesConfig {
}