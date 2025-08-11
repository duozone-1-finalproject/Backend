// ========== 7. UserInfo.java (DTO) ==========
package com.example.finalproject.login_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfo {
    private final String email;
    private final String name;
    private final String provider;
}