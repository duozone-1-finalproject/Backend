// 3. DartUnderwriterInfoRepository
package com.example.finalproject.apitest.repository;

import com.example.finalproject.apitest.entity.DartUnderwriterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DartUnderwriterInfoRepository extends JpaRepository<DartUnderwriterInfo, Long> {

    // 접수번호로 조회
    List<DartUnderwriterInfo> findByRceptNo(String rceptNo);

    // 기업코드로 조회
    List<DartUnderwriterInfo> findByCorpCode(String corpCode);

    // 인수인명으로 조회
    List<DartUnderwriterInfo> findByActnmnContaining(String actnmn);

    // 인수인구분으로 조회
    List<DartUnderwriterInfo> findByActsen(String actsen);

    // 인수금액 범위로 조회
    List<DartUnderwriterInfo> findByUdtamtBetween(Long minAmount, Long maxAmount);

    // 기업코드와 인수인구분으로 조회
    List<DartUnderwriterInfo> findByCorpCodeAndActsen(String corpCode, String actsen);

    // 인수인별 총 인수금액 조회
    @Query("SELECT d.actnmn, SUM(d.udtamt) FROM DartUnderwriterInfo d GROUP BY d.actnmn")
    List<Object[]> findTotalAmountByUnderwriter();
}
