package com.example.test_02.dto;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "비밀번호는 최소 8자 이상, 숫자와 문자를 포함해야 합니다.")
    private String password;

    @Email
    private String email;

    @NotBlank
    private String name;
}
