package com.example.Backend.config;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Service
public class SecretsManagerService {

    private final SecretsManagerClient client;

    public SecretsManagerService() {
        // DefaultCredentialsProvider는 환경 변수, 자바 시스템 속성, 프로필 파일,
        // ECS 컨테이너 자격 증명, EC2 인스턴스 프로필 자격 증명(EKS 서비스 계정 포함)에서
        // 자격 증명을 자동으로 찾습니다.
        this.client = SecretsManagerClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public String getSecret(String secretName) {
        GetSecretValueRequest request = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse response = client.getSecretValue(request);
        return response.secretString();
    }
}
