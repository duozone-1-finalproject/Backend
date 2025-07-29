package com.example.test_02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로컬 회원가입만 사용
    @Column(unique = true)
    private String username;

    private String password;

    // OAuth 방식과 로컬 방식 사용
    @Column(unique = true)
    private String email;

    private String name;

    private String role;

    // 네이버,구글,로컬 구별용 칼럼으로 각 계정으로 로그인시 자동으로 변경됨.
    private String provider;

}