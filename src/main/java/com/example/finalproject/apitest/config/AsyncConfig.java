package com.example.finalproject.apitest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // 비동기 기능 활성화를 위한 어노테이션
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 기본 스레드 수
        executor.setMaxPoolSize(30); // 최대 스레드 수 (API 개수가 많으므로 늘림)
        executor.setQueueCapacity(100); // 대기 큐 사이즈
        executor.setThreadNamePrefix("DartApi-");
        executor.initialize();
        return executor;
    }
}
