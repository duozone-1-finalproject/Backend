package com.example.finalproject.apitest.repository.periodic;
// 이사·감사 전체의 보수현황(주주총회 승인금액)
import com.example.finalproject.apitest.entity.periodic.DartCompensationApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartCompensationApprovalRepository extends JpaRepository<DartCompensationApproval, String> {
    /**
     * 고유번호(corpCode)로 이사/감사 보수현황 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartCompensationApproval> findByCorpCode(String corpCode);
}

