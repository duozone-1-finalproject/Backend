package com.example.test_02.dart.repository;

import com.example.dart.model.entity.CompanyOverview;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface CompanyOverviewRepository extends JpaRepository<CompanyOverview, String> {
    Optional<CompanyOverview> findByCorpCode(String corpCode);
    Optional<CompanyOverview> findByCorpName(String corpName);
    List<CompanyOverview> findAll();
}