package com.example.finalproject.apitest.repository.periodic;

import com.example.finalproject.apitest.entity.periodic.DartTreasuryStockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartTreasuryStockStatusRepository extends JpaRepository<DartTreasuryStockStatus, Long> {
    /**
     * 고유번호(corpCode)로 자기주식 취득 및 처분 현황 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 DartTreasuryStockStatus 엔티티 리스트
     */
    List<DartTreasuryStockStatus> findByCorpCode(String corpCode);
}
