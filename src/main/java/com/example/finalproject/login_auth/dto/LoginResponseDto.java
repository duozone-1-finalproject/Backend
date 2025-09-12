// dto/LoginResponseDto.java
package com.example.finalproject.login_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String username;
    private String email;
    private String name;
    private String role;
}