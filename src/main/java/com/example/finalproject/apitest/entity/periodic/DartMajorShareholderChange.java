package com.example.finalproject.apitest.entity.periodic;

// 최대주주 변동현황
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_major_shareholder_change")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DartMajorShareholderChange {

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

    @Column(name = "change_on")
    private LocalDate changeOn; // 변동 일

    @Column(name = "mxmm_shrhldr_nm", length = 200)
    private String mxmmShrhldrNm; // 최대 주주 명

    @Column(name = "posesn_stock_co")
    private Long posesnStockCo; // 소유 주식 수

    @Column(name = "qota_rt")
    private Double qotaRt; // 지분 율

    @Column(name = "change_cause", columnDefinition = "TEXT")
    private String changeCause; // 변동 원인

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