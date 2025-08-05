package com.example.finalproject.login_auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "users") // Elasticsearch 인덱스명
public class User {

    @Id
    private String id;  // Elasticsearch는 보통 String ID 사용

    @Field(type = FieldType.Keyword) // 정확히 일치하는 username 검색용
    private String username;

    @Field(type = FieldType.Text, index = false) // password는 검색할 일 없으므로 index 끔
    private String password;

    @Field(type = FieldType.Keyword) // 이메일도 키워드로 저장
    private String email;

    @Field(type = FieldType.Text) // full-text 검색이 필요할 수 있음
    private String name;

    @Field(type = FieldType.Keyword)
    private String role;

    @Field(type = FieldType.Keyword)
    private String provider;

}
