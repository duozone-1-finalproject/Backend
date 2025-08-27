package com.example.finalproject.apitest.repository.periodic;

import com.example.finalproject.apitest.entity.periodic.DartEmployeeStatus;
import com.example.finalproject.apitest.entity.periodic.DartExecutiveStatus;
import com.example.finalproject.apitest.entity.periodic.DartMajorShareholderChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 직원 현황
public interface DartEmployeeStatusRepository extends JpaRepository<DartEmployeeStatus, Long> {
    List<DartEmployeeStatus> findByCorpCode(String corpCode);
}