package com.example.finalproject.apitest.repository.periodic;
import com.example.finalproject.apitest.entity.periodic.DartExecutiveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 임원 현황
public interface DartExecutiveStatusRepository extends JpaRepository<DartExecutiveStatus, Long> {
    List<DartExecutiveStatus> findByCorpCode(String corpCode);
}
