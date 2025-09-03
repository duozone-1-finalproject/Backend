package com.example.finalproject.apitest.repository.periodic;
// 신종자본증권 미상환 잔액
import com.example.finalproject.apitest.entity.periodic.DartHybridSecuritiesBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartHybridSecuritiesBalanceRepository extends JpaRepository<DartHybridSecuritiesBalance, String> {
    /**
     * 고유번호(corpCode)로 신종자본증권 미상환 잔액 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartHybridSecuritiesBalance> findByCorpCode(String corpCode);
}
