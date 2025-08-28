package com.example.finalproject.apitest.repository.overview;

import com.example.finalproject.apitest.entity.overview.CompanyOverview2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyOverviewRepository2 extends JpaRepository<CompanyOverview2, Long> {

    /**
     * 고유번호로 기업개황 조회
     */
    Optional<CompanyOverview2> findByCorpCode(String corpCode);

    /**
     * 회사명으로 기업개황 조회 (부분 매치)
     */
    List<CompanyOverview2> findByCorpNameContainingIgnoreCase(String corpName);

    /**
     * 종목코드로 기업개황 조회
     */
    Optional<CompanyOverview2> findByStockCode(String stockCode);

    /**
     * 법인구분별 기업 목록 조회
     */
    List<CompanyOverview2> findByCorpCls(String corpCls);

    /**
     * 업종코드별 기업 목록 조회
     */
    List<CompanyOverview2> findByIndutyCode(String indutyCode);

    /**
     * 대표자명으로 기업 검색
     */
    List<CompanyOverview2> findByCeoNmContainingIgnoreCase(String ceoNm);

    /**
     * 설립일 범위로 기업 검색
     */
    List<CompanyOverview2> findByEstDtBetween(String startDate, String endDate);

    /**
     * 상장회사만 조회 (종목코드가 있는 회사)
     */
    @Query("SELECT co FROM CompanyOverview2 co WHERE co.stockCode IS NOT NULL AND co.stockCode != ''")
    List<CompanyOverview2> findListedCompanies();

    /**
     * 법인구분별 카운트
     */
    @Query("SELECT co.corpCls, COUNT(co) FROM CompanyOverview2 co GROUP BY co.corpCls")
    List<Object[]> countByCorpCls();

    /**
     * 업종별 카운트
     */
    @Query("SELECT co.indutyCode, COUNT(co) FROM CompanyOverview2 co WHERE co.indutyCode IS NOT NULL GROUP BY co.indutyCode")
    List<Object[]> countByIndutyCode();

    /**
     * 최근 업데이트된 기업 목록 조회
     */
    List<CompanyOverview2> findTop10ByOrderByUpdatedAtDesc();

    /**
     * 특정 기간 내 생성된 기업 목록
     */
    List<CompanyOverview2> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 회사명과 법인구분으로 검색
     */
    List<CompanyOverview2> findByCorpNameContainingIgnoreCaseAndCorpCls(String corpName, String corpCls);

    /**
     * 결산월별 기업 조회
     */
    List<CompanyOverview2> findByAccMt(String accMt);

    /**
     * 고유번호 존재 여부 확인
     */
    boolean existsByCorpCode(String corpCode);

    /**
     * 종목코드 존재 여부 확인
     */
    boolean existsByStockCode(String stockCode);

    /**
     * 복합 검색 쿼리
     */
    @Query("SELECT co FROM CompanyOverview2 co WHERE " +
            "(:corpName IS NULL OR UPPER(co.corpName) LIKE UPPER(CONCAT('%', :corpName, '%'))) AND " +
            "(:corpCls IS NULL OR co.corpCls = :corpCls) AND " +
            "(:indutyCode IS NULL OR co.indutyCode = :indutyCode) AND " +
            "(:ceoNm IS NULL OR UPPER(co.ceoNm) LIKE UPPER(CONCAT('%', :ceoNm, '%')))")
    List<CompanyOverview2> searchCompanies(@Param("corpName") String corpName,
                                           @Param("corpCls") String corpCls,
                                           @Param("indutyCode") String indutyCode,
                                           @Param("ceoNm") String ceoNm);
}