package com.example.finalproject.apitest.repository.periodic;
// 단일회사 전체 재무제표
import com.example.finalproject.apitest.entity.periodic.DartNonConsolidatedFinancialStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartNonConsolidatedFinancialStatementRepository extends JpaRepository<DartNonConsolidatedFinancialStatement, Long> {
    /**
     * 고유번호(corpCode)로 단일회사 전체 재무제표 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartNonConsolidatedFinancialStatement> findByCorpCode(String corpCode);
}
