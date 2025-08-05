package com.example.finalproject.dart.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.*;

@Document(indexName = "company_overview") // Elasticsearch 인덱스명
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyOverview {

    @Id
    private String corpCode;

    @Field(type = FieldType.Keyword)
    private String corpName;

    @Field(type = FieldType.Keyword)
    private String corpCls;

    @Field(type = FieldType.Text)
    private String adres;

    @Field(type = FieldType.Keyword)
    private String hmUrl;

    @Field(type = FieldType.Keyword)
    private String indutyCode;

    @Field(type = FieldType.Keyword)
    private String indutyName;

    @Field(type = FieldType.Keyword)
    private String estDt;

    @Field(type = FieldType.Integer)
    private Integer favoriteCount;

    @Field(type = FieldType.Keyword)
    private String logo;
}
