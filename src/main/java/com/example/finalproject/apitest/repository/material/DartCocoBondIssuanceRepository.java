package com.example.finalproject.apitest.repository.material;
// 상각형 조건부자본증권 발행결정
import com.example.finalproject.apitest.entity.material.DartCocoBondIssuance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DartCocoBondIssuanceRepository extends JpaRepository<DartCocoBondIssuance, Long> {
    /**
     * 고유번호(corpCode)로 상각형 조건부자본증권 발행결정 목록을 조회합니다.
     * @param corpCode 회사의 고유번호
     * @return 조회된 엔티티 리스트
     */
    List<DartCocoBondIssuance> findByCorpCode(String corpCode);
}
