package com.example.finalproject;

import com.example.finalproject.config.DartProperties;
import com.example.finalproject.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

// By placing @SpringBootApplication in the root package, explicit @ComponentScan is no longer needed.
@EnableAsync
@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, DartProperties.class})
public class BackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
