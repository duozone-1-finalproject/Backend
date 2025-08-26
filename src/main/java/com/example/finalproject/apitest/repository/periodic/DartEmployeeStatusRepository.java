package com.example.finalproject.apitest.repository.periodic;

import com.example.finalproject.apitest.entity.periodic.DartEmployeeStatus;
import com.example.finalproject.apitest.entity.periodic.DartMajorShareholderChange;
import org.springframework.data.jpa.repository.JpaRepository;

// 직원 현황
public interface DartEmployeeStatusRepository extends JpaRepository<DartEmployeeStatus, Long> {
}
