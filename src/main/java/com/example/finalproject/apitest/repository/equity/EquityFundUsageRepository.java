package com.example.finalproject.apitest.repository.equity;
import com.example.finalproject.apitest.entity.equity.EquityFundUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface EquityFundUsageRepository extends JpaRepository<EquityFundUsage, Long> {
    List<EquityFundUsage> findByCorpCode(String corpCode);
}