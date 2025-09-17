package com.example.finalproject.apitest.repository.periodic;
// 미등기임원 보수현황
import com.example.finalproject.apitest.entity.periodic.DartUnregisteredExecutiveCompensation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartUnregisteredExecutiveCompensationRepository extends JpaRepository<DartUnregisteredExecutiveCompensation, String> {
    /**
     * 고유번호(corpCode)로 미등기임원 보수현황 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartUnregisteredExecutiveCompensation> findByCorpCode(String corpCode);
}

