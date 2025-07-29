package com.example.finalproject.dart.repository;

import com.example.finalproject.dart.entity.CompanyOverview;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface CompanyOverviewRepository extends JpaRepository<CompanyOverview, String> {
    Optional<CompanyOverview> findByCorpCode(String corpCode);
    Optional<CompanyOverview> findByCorpName(String corpName);
    List<CompanyOverview> findAll();
}