package com.example.finalproject.apitest.repository.periodic;
// 공모자금의 사용내역
import com.example.finalproject.apitest.entity.periodic.DartPublicOfferingFundUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartPublicOfferingFundUsageRepository extends JpaRepository<DartPublicOfferingFundUsage, Long> {
    /**
     * 고유번호(corpCode)로 공모자금의 사용내역 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartPublicOfferingFundUsage> findByCorpCode(String corpCode);
}
