package com.example.finalproject;

import com.example.finalproject.login_auth.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class Test02Application {
	public static void main(String[] args) {
		SpringApplication.run(Test02Application.class, args);
	}
}
