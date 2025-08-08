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
    @Field(name = "corp_code", type = FieldType.Keyword)
    private String corpCode;

    @Field(name = "corp_name", type = FieldType.Keyword)
    private String corpName;

    @Field(name = "corp_eng_name", type = FieldType.Keyword)
    private String corpEngName;

    @Field(name = "stock_code", type = FieldType.Keyword)
    private String stockCode;

}
