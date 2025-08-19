// 6. DartRedemptionRightsRepository
package com.example.finalproject.apitest.repository;

import com.example.finalproject.apitest.entity.DartRedemptionRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DartRedemptionRightsRepository extends JpaRepository<DartRedemptionRights, Long> {

    // 접수번호로 조회
    List<DartRedemptionRights> findByRceptNo(String rceptNo);

    // 기업코드로 조회
    List<DartRedemptionRights> findByCorpCode(String corpCode);

    // 부여사유로 조회
    List<DartRedemptionRights> findByGrtrsContaining(String grtrs);

    // 행사가능 투자자로 조회
    List<DartRedemptionRights> findByExavivrContaining(String exavivr);

    // 행사가격 범위로 조회
    List<DartRedemptionRights> findByExprcBetween(Long minPrice, Long maxPrice);

    // 부여수량 범위로 조회
    List<DartRedemptionRights> findByGrtcntBetween(Long minCount, Long maxCount);

    // 기업별 총 부여수량 조회
    @Query("SELECT d.corpName, SUM(d.grtcnt) FROM DartRedemptionRights d GROUP BY d.corpName")
    List<Object[]> findTotalGrantedByCompany();
}