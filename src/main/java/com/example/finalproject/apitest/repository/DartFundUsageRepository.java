// 4. DartFundUsageRepository
package com.example.finalproject.apitest.repository;

import com.example.finalproject.apitest.entity.DartFundUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DartFundUsageRepository extends JpaRepository<DartFundUsage, Long> {

    // 접수번호로 조회
    List<DartFundUsage> findByRceptNo(String rceptNo);

    // 기업코드로 조회
    List<DartFundUsage> findByCorpCode(String corpCode);

    // 자금사용 구분으로 조회
    List<DartFundUsage> findBySeContaining(String se);

    // 금액 범위로 조회
    List<DartFundUsage> findByAmtBetween(Long minAmount, Long maxAmount);

    // 기업별 자금사용 총액 조회
    @Query("SELECT d.corpName, SUM(d.amt) FROM DartFundUsage d GROUP BY d.corpName")
    List<Object[]> findTotalAmountByCompany();

    // 자금사용 목적별 총액 조회
    @Query("SELECT d.se, SUM(d.amt) FROM DartFundUsage d GROUP BY d.se")
    List<Object[]> findTotalAmountByUsage();
}