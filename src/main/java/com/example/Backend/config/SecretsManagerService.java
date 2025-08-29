package com.example.backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.Map;

@Service
public class SecretsManagerService {

    private final SecretsManagerClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public SecretsManagerService() {
        this.client = SecretsManagerClient.builder()
                .region(Region.AP_NORTHEAST_2) // 서울 리전
                .build();
    }

    public Map<String, String> getSecretMap(String secretName) {
        GetSecretValueResponse response = client.getSecretValue(
                GetSecretValueRequest.builder().secretId(secretName).build()
        );

        try {
            return mapper.readValue(response.secretString(), new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse secret JSON", e);
        }
    }
}
