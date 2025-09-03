// entity/User.java
package com.example.finalproject.login_auth.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    private String password;

    private String role;

    private String provider; // local, google, naver, kakao
}
