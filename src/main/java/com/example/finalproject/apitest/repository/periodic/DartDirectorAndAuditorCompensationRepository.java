package com.example.finalproject.apitest.repository.periodic;
// 이사·감사 전체의 보수현황(보수지급금액 - 유형별)
import com.example.finalproject.apitest.entity.periodic.DartDirectorAndAuditorCompensation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartDirectorAndAuditorCompensationRepository extends JpaRepository<DartDirectorAndAuditorCompensation, String> {
    /**
     * 고유번호(corpCode)로 이사/감사 보수지급금액 현황 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartDirectorAndAuditorCompensation> findByCorpCode(String corpCode);
}
