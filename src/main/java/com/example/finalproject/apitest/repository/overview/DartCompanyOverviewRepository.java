package com.example.finalproject.apitest.repository.overview;

import com.example.finalproject.apitest.entity.overview.DartCompanyOverview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DartCompanyOverviewRepository extends JpaRepository<DartCompanyOverview, String> {
}

