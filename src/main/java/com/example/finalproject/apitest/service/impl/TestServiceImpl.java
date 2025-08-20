package com.example.finalproject.apitest.service.impl;

import com.example.finalproject.apitest.config.DartApiProperties;
import com.example.finalproject.apitest.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

    private final RestClient client;
    private final String apiKey;

    public TestServiceImpl(DartApiProperties dartApiProperties){
        this.client = RestClient.builder()
                .baseUrl(dartApiProperties.getBaseUrl())
                .build();
        this.apiKey = dartApiProperties.getKey();
    }

    @Override
    public String testServ(){
        return "";
    }
}
