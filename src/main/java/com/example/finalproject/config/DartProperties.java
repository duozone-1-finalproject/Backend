package com.example.finalproject.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "dart")
public class DartProperties {
    private String apiKey;
    private String apiKeyObj;
    private String apiKeyLjh;
    private String Url;
}