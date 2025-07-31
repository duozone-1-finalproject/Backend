package com.example.finalproject.login_auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "users")
public class User {

    @Id
    private String id;

    private String username;

    private String password;

    private String email;

    private String name;

    private String role;

    private String provider;
}
