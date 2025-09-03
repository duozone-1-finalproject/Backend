package com.example.finalproject.apitest.repository.periodic;
// 감사용역체결현황
import com.example.finalproject.apitest.entity.periodic.DartAuditServiceContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartAuditServiceContractRepository extends JpaRepository<DartAuditServiceContract, Long> {
    /**
     * 고유번호(corpCode)로 감사용역체결현황 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartAuditServiceContract> findByCorpCode(String corpCode);
}
