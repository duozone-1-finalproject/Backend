package com.example.finalproject.apitest.repository.periodic;

import com.example.finalproject.apitest.entity.periodic.DartTotalStockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 주식의 총수 현황
@Repository
public interface DartTotalStockStatusRepository extends JpaRepository<DartTotalStockStatus, Long> {
    // JpaRepository<엔티티클래스, ID필드타입>
}