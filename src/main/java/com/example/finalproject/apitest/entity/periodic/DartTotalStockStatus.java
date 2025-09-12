package com.example.finalproject.apitest.entity.periodic;

// 주식의 총수 현황
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_total_stock_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartTotalStockStatus {

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
    private String corpName; // 회사명

    @Column(name = "se", length = 200)
    private String se; // 구분

    @Column(name = "isu_stock_totqy")
    private Long isuStockTotqy; // 발행할 주식의 총수

    @Column(name = "now_to_isu_stock_totqy")
    private Long nowToIsuStockTotqy; // 현재까지 발행한 주식의 총수

    @Column(name = "now_to_dcrs_stock_totqy")
    private Long nowToDcrsStockTotqy; // 현재까지 감소한 주식의 총수

    @Column(name = "redc")
    private Long redc; // 감자

    @Column(name = "profit_incnr")
    private Long profitIncnr; // 이익소각

    @Column(name = "rdmstk_repy")
    private Long rdmstkRepy; // 상환주식의 상환

    @Column(name = "etc")
    private Long etc; // 기타

    @Column(name = "istc_totqy")
    private Long istcTotqy; // 발행주식의 총수

    @Column(name = "tesstk_co")
    private Long tesstkCo; // 자기주식수

    @Column(name = "distb_stock_co")
    private Long distbStockCo; // 유통주식수

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