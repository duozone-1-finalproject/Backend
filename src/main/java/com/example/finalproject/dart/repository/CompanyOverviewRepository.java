package com.example.finalproject.dart.repository;

import com.example.finalproject.dart.entity.CompanyOverview;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;
import java.util.List;

public interface CompanyOverviewRepository extends ElasticsearchRepository<CompanyOverview, String> {
    Optional<CompanyOverview> findByCorpCode(String corpCode);
    Optional<CompanyOverview> findByCorpName(String corpName);
    List<CompanyOverview> findAll();
}
