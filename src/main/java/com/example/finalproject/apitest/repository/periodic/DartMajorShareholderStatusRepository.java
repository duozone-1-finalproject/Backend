package com.example.finalproject.apitest.repository.periodic;

import com.example.finalproject.apitest.entity.periodic.DartMajorShareholderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 최대주주 현황
@Repository
public interface DartMajorShareholderStatusRepository extends JpaRepository<DartMajorShareholderStatus, Long> {
    // JpaRepository<엔티티클래스, ID필드타입>

    List<DartMajorShareholderStatus> findByCorpCode(String corpCode);
}