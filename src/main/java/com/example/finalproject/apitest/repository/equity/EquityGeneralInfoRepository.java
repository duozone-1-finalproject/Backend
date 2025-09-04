package com.example.finalproject.apitest.repository.equity;
import com.example.finalproject.apitest.entity.equity.EquityGeneralInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EquityGeneralInfoRepository extends JpaRepository<EquityGeneralInfo, Long> {
    List<EquityGeneralInfo> findByCorpCode(String corpCode);
}