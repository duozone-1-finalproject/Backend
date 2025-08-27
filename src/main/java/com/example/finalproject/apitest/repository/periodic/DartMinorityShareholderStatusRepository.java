package com.example.finalproject.apitest.repository.periodic;

import com.example.finalproject.apitest.entity.periodic.DartMinorityShareholderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// 소액주주 현황
@Repository
public interface DartMinorityShareholderStatusRepository extends JpaRepository<DartMinorityShareholderStatus, Long> {
    // JpaRepository<엔티티클래스, ID필드타입>

    List<DartMinorityShareholderStatus> findByCorpCode(String corpCode);
}