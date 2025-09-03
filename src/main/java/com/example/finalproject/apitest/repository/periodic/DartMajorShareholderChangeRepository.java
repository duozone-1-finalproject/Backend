package com.example.finalproject.apitest.repository.periodic;

import com.example.finalproject.apitest.entity.periodic.DartMajorShareholderChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// 최대주주 변동현황
@Repository
public interface DartMajorShareholderChangeRepository extends JpaRepository<DartMajorShareholderChange, Long> {
    List<DartMajorShareholderChange> findByCorpCode(String corpCode);
    // JpaRepository<엔티티클래스, ID필드타입>
}