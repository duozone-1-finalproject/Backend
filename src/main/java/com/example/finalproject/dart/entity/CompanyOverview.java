package com.example.finalproject.dart.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "company_overview")
public class CompanyOverview {

    @Id
    private String corpCode;

    private String corpName;
}
