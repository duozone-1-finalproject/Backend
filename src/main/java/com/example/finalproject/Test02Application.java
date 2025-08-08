package com.example.finalproject;

import com.example.finalproject.login_auth.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
// 브랜치 테스트용 커밋 주석
@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class Test02Application {
	public static void main(String[] args) {
		SpringApplication.run(Test02Application.class, args);
	}
}
