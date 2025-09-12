package com.example.finalproject.apitest.repository.equity;
import com.example.finalproject.apitest.entity.equity.EquityGeneralInfo;
import com.example.finalproject.apitest.entity.equity.EquitySecurityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface EquitySecurityTypeRepository extends JpaRepository<EquitySecurityType, Long> {
    List<EquitySecurityType> findByCorpCode(String corpCode);
}