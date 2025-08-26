package com.example.finalproject.apitest.repository.periodic;
// 사모자금의 사용내역
import com.example.finalproject.apitest.entity.periodic.DartPrivatePlacementFundUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartPrivatePlacementFundUsageRepository extends JpaRepository<DartPrivatePlacementFundUsage, Long> {
    /**
     * 고유번호(corpCode)로 사모자금의 사용내역 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartPrivatePlacementFundUsage> findByCorpCode(String corpCode);
}

