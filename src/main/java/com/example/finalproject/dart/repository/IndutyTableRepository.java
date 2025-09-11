package com.example.finalproject.dart.repository;

import com.example.finalproject.dart.dto.IndutyTableResponseDto;
import com.example.finalproject.dart.entity.IndutyTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndutyTableRepository  extends JpaRepository<IndutyTable, Long> {
    IndutyTable findOneByIndutyCode();
}