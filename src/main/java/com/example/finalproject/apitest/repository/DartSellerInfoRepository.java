// 5. DartSellerInfoRepository
package com.example.finalproject.apitest.repository;

import com.example.finalproject.apitest.entity.equity.DartSellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DartSellerInfoRepository extends JpaRepository<DartSellerInfo, Long> {

    // 접수번호로 조회
    List<DartSellerInfo> findByRceptNo(String rceptNo);

    // 기업코드로 조회
    List<DartSellerInfo> findByCorpCode(String corpCode);

    // 보유자명으로 조회
    List<DartSellerInfo> findByHdrContaining(String hdr);

    // 회사와의 관계로 조회
    List<DartSellerInfo> findByRlCmp(String rlCmp);

    // 매출증권수 범위로 조회
    List<DartSellerInfo> findBySlstkBetween(Long minCount, Long maxCount);

    // 기업별 총 매출증권수 조회
    @Query("SELECT d.corpName, SUM(d.slstk) FROM DartSellerInfo d GROUP BY d.corpName")
    List<Object[]> findTotalSoldStocksByCompany();
}