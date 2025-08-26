package com.example.finalproject.apitest.repository.material;

import com.example.finalproject.apitest.entity.material.DartCbIssuance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 전환사채권 발행결정
@Repository
public interface DartCbIssuanceRepository extends JpaRepository<DartCbIssuance, Long> {
    // JpaRepository<엔티티클래스, ID필드타입>
    List<DartCbIssuance> findByCorpCode(String corpCode);
}