package com.example.finalproject.apitest.repository.periodic;
// 회사채 미상환 잔액
import com.example.finalproject.apitest.entity.periodic.DartCorporateBondBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartCorporateBondBalanceRepository extends JpaRepository<DartCorporateBondBalance, String> {
    /**
     * 고유번호(corpCode)로 회사채 미상환 잔액 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartCorporateBondBalance> findByCorpCode(String corpCode);
}
