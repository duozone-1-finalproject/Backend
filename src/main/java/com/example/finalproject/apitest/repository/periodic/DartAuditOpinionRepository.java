package com.example.finalproject.apitest.repository.periodic;
// 회계감사인의 명칭 및 감사의견
import com.example.finalproject.apitest.entity.periodic.DartAuditOpinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartAuditOpinionRepository extends JpaRepository<DartAuditOpinion, String> {
    /**
     * 고유번호(corpCode)로 회계감사인의 명칭 및 감사의견 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartAuditOpinion> findByCorpCode(String corpCode);
}
