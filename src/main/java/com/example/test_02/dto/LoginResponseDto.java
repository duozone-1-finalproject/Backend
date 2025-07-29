// src/main/java/com/example/test_02/dto/LoginResponseDto.java
package com.example.test_02.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String username;
    private String email;
    private String name;
    private String role;
}