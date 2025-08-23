package com.example.finalproject;

import com.example.finalproject.login_auth.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

// 브랜치 테스트용 커밋 주석
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.finalproject.apitest", "com.example.finalproject.login_auth", "com.example.finalproject.dart","com.example.finalproject.dart_viewer"})
@EnableConfigurationProperties(JwtProperties.class)
public class Test02Application {
	public static void main(String[] args) {
		SpringApplication.run(Test02Application.class, args);
	}
}
