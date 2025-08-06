package com.example.finalproject.dart.repository;
import com.example.finalproject.dart.entity.CompanyOverview;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CompanyOverviewRepository extends ElasticsearchRepository<CompanyOverview, String> {
}
