package com.example.finalproject.apitest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // Spring의 비동기 메소드 기능을 활성화합니다.
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 동시에 실행시킬 스레드의 수
        executor.setMaxPoolSize(20);  // 최대 스레드 수
        executor.setQueueCapacity(100); // 작업 대기 큐의 크기
        executor.setThreadNamePrefix("DartApi-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;
    }
}
