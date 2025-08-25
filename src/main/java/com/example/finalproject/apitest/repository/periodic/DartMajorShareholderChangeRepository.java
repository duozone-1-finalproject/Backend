package com.example.finalproject.apitest.repository.periodic;

import com.example.finalproject.apitest.entity.periodic.DartMajorShareholderChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 최대주주 변동현황
@Repository
public interface DartMajorShareholderChangeRepository extends JpaRepository<DartMajorShareholderChange, Long> {
    // JpaRepository<엔티티클래스, ID필드타입>
}