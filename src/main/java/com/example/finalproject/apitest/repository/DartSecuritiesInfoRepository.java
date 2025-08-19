// 2. DartSecuritiesInfoRepository
package com.example.finalproject.apitest.repository;

import com.example.finalproject.apitest.entity.equity.DartSecuritiesInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DartSecuritiesInfoRepository extends JpaRepository<DartSecuritiesInfo, Long> {

    // 접수번호로 조회
    List<DartSecuritiesInfo> findByRceptNo(String rceptNo);

    // 기업코드로 조회
    List<DartSecuritiesInfo> findByCorpCode(String corpCode);

    // 증권종류로 조회
    List<DartSecuritiesInfo> findByStksenContaining(String stksen);

    // 모집총액 범위로 조회
    List<DartSecuritiesInfo> findBySltaBetween(Long minAmount, Long maxAmount);

    // 기업명과 증권종류로 조회
    List<DartSecuritiesInfo> findByCorpNameAndStksen(String corpName, String stksen);
}