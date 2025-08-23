package com.example.finalproject.apitest.entity.periodic;

// 최대주주 현황
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_major_shareholder_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DartMajorShareholderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rcept_no", length = 14, nullable = false)
    private String rceptNo; // 접수번호

    @Column(name = "corp_cls", length = 1)
    private String corpCls; // 법인구분

    @Column(name = "corp_code", length = 8)
    private String corpCode; // 고유번호

    @Column(name = "corp_name", length = 200)
    private String corpName; // 법인명

    @Column(name = "nm", length = 200)
    private String nm; // 성명

    @Column(name = "relate", length = 200)
    private String relate; // 관계

    @Column(name = "stock_knd", length = 200)
    private String stockKnd; // 주식 종류

    @Column(name = "bsis_posesn_stock_co")
    private Long bsisPosesnStockCo; // 기초 소유 주식 수

    @Column(name = "bsis_posesn_stock_qota_rt")
    private Double bsisPosesnStockQotaRt; // 기초 소유 주식 지분 율

    @Column(name = "trmend_posesn_stock_co")
    private Long trmendPosesnStockCo; // 기말 소유 주식 수

    @Column(name = "trmend_posesn_stock_qota_rt")
    private Double trmendPosesnStockQotaRt; // 기말 소유 주식 지분 율

    @Column(name = "rm", columnDefinition = "TEXT")
    private String rm; // 비고

    @Column(name = "stlm_dt")
    private LocalDate stlmDt; // 결산기준일

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}