package com.example.finalproject.apitest.repository.equity;
import com.example.finalproject.apitest.entity.equity.EquityUnderwriterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface EquityUnderwriterInfoRepository extends JpaRepository<EquityUnderwriterInfo, Long> {
    List<EquityUnderwriterInfo> findByCorpCode(String corpCode);
}