// src/main/java/com/example/finalproject/ai_backend/repository/IndustryTableRepository.java
package com.example.finalproject.ai_backend.repository;

import com.example.finalproject.ai_backend.entity.IndustryTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndustryTableRepository extends JpaRepository<IndustryTable, String> {

    /**
     * 업종코드로 업종명 조회
     */
    @Query("SELECT i.indutyName FROM IndustryTable i WHERE i.indutyCode = :indutyCode")
    Optional<String> findIndutyNameByCode(@Param("indutyCode") String indutyCode);

    /**
     * 기본 조회 (문제 발생시 대안)
     */
    @Query("SELECT i FROM IndustryTable i WHERE i.indutyCode = :indutyCode")
    Optional<IndustryTable> findByIndutyCodeCustom(@Param("indutyCode") String indutyCode);
}