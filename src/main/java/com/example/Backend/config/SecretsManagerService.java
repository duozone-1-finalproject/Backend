package com.example.Backend.config;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Service
public class SecretsManagerService {

    private final SecretsManagerClient secretsManagerClient;

    /**
     * EKS 환경에서는 Service Account에 연결된 IAM Role의 자격증명을,
     * 로컬에서는 ~/.aws/credentials의 자격증명을 자동으로 사용합니다.
     */
    public SecretsManagerService() {
        this.secretsManagerClient = SecretsManagerClient.builder()
                .region(Region.AP_NORTHEAST_2) // 사용하시는 AWS 리전으로 변경
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public String getSecret(String secretName) {
        GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse valueResponse = secretsManagerClient.getSecretValue(valueRequest);
        return valueResponse.secretString();
    }
}