package com.example.finalproject.apitest.repository.equity;
import com.example.finalproject.apitest.entity.equity.EquityRepurchaseOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface EquityRepurchaseOptionRepository extends JpaRepository<EquityRepurchaseOption, Long> {
    List<EquityRepurchaseOption> findByCorpCode(String corpCode);
}