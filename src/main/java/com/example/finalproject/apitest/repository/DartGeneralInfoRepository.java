// 1. DartGeneralInfoRepository
package com.example.finalproject.apitest.repository;

import com.example.finalproject.apitest.entity.equity.DartGeneralInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DartGeneralInfoRepository extends JpaRepository<DartGeneralInfo, Long> {


    // 접수번호로 조회
    Optional<DartGeneralInfo> findByRceptNo(String rceptNo);

    // 접수번호 존재 여부 확인
    boolean existsByRceptNo(String rceptNo);

    // 기업코드로 조회
    List<DartGeneralInfo> findByCorpCode(String corpCode);

    // 기업명으로 조회
    List<DartGeneralInfo> findByCorpNameContaining(String corpName);

    // 청약기일 기간으로 조회
    List<DartGeneralInfo> findBySbdBetween(LocalDate startDate, LocalDate endDate);

    // 최신 데이터 조회
    @Query("SELECT d FROM DartGeneralInfo d ORDER BY d.createdAt DESC")
    List<DartGeneralInfo> findLatest();
}
