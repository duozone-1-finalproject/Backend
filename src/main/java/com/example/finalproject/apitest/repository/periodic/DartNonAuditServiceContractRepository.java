package com.example.finalproject.apitest.repository.periodic;
// 회계감사인과의 비감사용역 계약체결 현황
import com.example.finalproject.apitest.entity.periodic.DartNonAuditServiceContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartNonAuditServiceContractRepository extends JpaRepository<DartNonAuditServiceContract, Long> {
    /**
     * 고유번호(corpCode)로 비감사용역 계약체결 현황 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartNonAuditServiceContract> findByCorpCode(String corpCode);
}
