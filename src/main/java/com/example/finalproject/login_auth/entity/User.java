package com.example.finalproject.login_auth.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.DateFormat;

import java.time.LocalDateTime;

@Document(indexName = "users") // MySQL @Entity 대신 @Document 사용
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id // JPA @Id 대신 Elasticsearch @Id 사용
    private String id; // MySQL의 Long id 대신 String 사용

    @Field(type = FieldType.Keyword) // 정확한 매칭을 위해 Keyword 타입
    private String username;

    @Field(type = FieldType.Text) // 암호화된 패스워드는 Text로
    private String password;

    @Field(type = FieldType.Keyword) // 이메일은 정확한 매칭 필요
    private String email;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Keyword)
    private String provider;

    @Field(type = FieldType.Keyword)
    private String role;

    // 리프레시 토큰 저장을 위한 필드만 유지
    @Field(type = FieldType.Text)
    private String refreshToken;

    @Field(type = FieldType.Keyword) // String으로 저장하여 변환 문제 해결
    private String refreshTokenExpiryDate;

    // LocalDateTime으로 변환하는 헬퍼 메서드
    public LocalDateTime getRefreshTokenExpiryDateAsLocalDateTime() {
        return refreshTokenExpiryDate != null ?
                LocalDateTime.parse(refreshTokenExpiryDate, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    public void setRefreshTokenExpiryDate(LocalDateTime dateTime) {
        this.refreshTokenExpiryDate = dateTime != null ?
                dateTime.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }
}