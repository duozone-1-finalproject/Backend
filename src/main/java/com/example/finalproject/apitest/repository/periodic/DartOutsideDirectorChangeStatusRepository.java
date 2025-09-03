package com.example.finalproject.apitest.repository.periodic;
// 사외이사 및 그 변동현황
import com.example.finalproject.apitest.entity.periodic.DartOutsideDirectorChangeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartOutsideDirectorChangeStatusRepository extends JpaRepository<DartOutsideDirectorChangeStatus, String> {
    /**
     * 고유번호(corpCode)로 사외이사 및 그 변동현황 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartOutsideDirectorChangeStatus> findByCorpCode(String corpCode);
}
