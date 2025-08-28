package com.example.finalproject.apitest.repository.material;

import com.example.finalproject.apitest.entity.material.DartBwIssuance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 신주인수권부사채권 발행결정
@Repository
public interface DartBwIssuanceRepository extends JpaRepository<DartBwIssuance, Long> {
    // JpaRepository<엔티티클래스, ID필드타입>
}