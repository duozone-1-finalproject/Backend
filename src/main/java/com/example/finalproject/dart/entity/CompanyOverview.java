package com.example.finalproject.dart.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "company_overview")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyOverview {

    @Id
    @Field(type = FieldType.Keyword, name = "corp_code")
    private String corpCode;

    @Field(type = FieldType.Keyword, name = "corp_name")
    private String corpName;

    @Field(type = FieldType.Keyword, name = "corp_cls")
    private String corpCls;

    @Field(type = FieldType.Text, name = "adres")
    private String adres;

    @Field(type = FieldType.Keyword, name = "hm_url")
    private String hmUrl;

    @Field(type = FieldType.Keyword, name = "induty_code")
    private String indutyCode;

    @Field(type = FieldType.Text, name = "induty_name")
    private String indutyName;

    @Field(type = FieldType.Keyword, name = "est_dt")
    private String estDt;

    @Field(type = FieldType.Integer, name = "favorite_count")
    private Integer favoriteCount;

}

