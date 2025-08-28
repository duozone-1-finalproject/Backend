package com.example.finalproject.apitest.entity.periodic;

// 소액주주 현황
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_minority_shareholder_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartMinorityShareholderStatus {

    @Id
    @Column(name = "rcept_no", length = 14, nullable = false)
    private String rceptNo; // 접수번호

    @Column(name = "corp_cls", length = 1)
    private String corpCls; // 법인구분

    @Column(name = "corp_code", length = 8)
    private String corpCode; // 고유번호

    @Column(name = "corp_name", length = 200)
    private String corpName; // 법인명

    @Column(name = "se", length = 200)
    private String se; // 구분 (소액주주)

    @Column(name = "shrholdr_co")
    private Long shrholdrCo; // 주주 수

    @Column(name = "shrholdr_tot_co")
    private Long shrholdrTotCo; // 전체 주주 수

    @Column(name = "shrholdr_rate")
    private Double shrholdrRate; // 주주 비율

    @Column(name = "hold_stock_co")
    private Long holdStockCo; // 보유 주식 수

    @Column(name = "stock_tot_co")
    private Long stockTotCo; // 총발행 주식 수

    @Column(name = "hold_stock_rate")
    private Double holdStockRate; // 보유 주식 비율

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