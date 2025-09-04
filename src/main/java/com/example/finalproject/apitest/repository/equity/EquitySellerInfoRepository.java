package com.example.finalproject.apitest.repository.equity;
import com.example.finalproject.apitest.entity.equity.EquitySellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface EquitySellerInfoRepository extends JpaRepository<EquitySellerInfo, Long> {
    List<EquitySellerInfo> findByCorpCode(String corpCode);
}