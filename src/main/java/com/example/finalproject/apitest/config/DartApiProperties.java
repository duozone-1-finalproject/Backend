package com.example.apitest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "dart.api")
public class DartApiProperties {
    // application.yml의 dart.api.key / dart.api.base-url 바인딩
    private String key;
    private String baseUrl;
}
