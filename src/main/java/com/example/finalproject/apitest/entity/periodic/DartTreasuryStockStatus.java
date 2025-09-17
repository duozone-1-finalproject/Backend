package com.example.finalproject.apitest.entity.periodic;

// 자기주식 취득 및 처분 현황
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_treasury_stock_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartTreasuryStockStatus {

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

    @Column(name = "acqs_mth1", length = 200)
    private String acqsMth1; // 취득방법 대분류

    @Column(name = "acqs_mth2", length = 200)
    private String acqsMth2; // 취득방법 중분류

    @Column(name = "acqs_mth3", length = 200)
    private String acqsMth3; // 취득방법 소분류

    @Column(name = "stock_knd", length = 200)
    private String stockKnd; // 주식 종류

    @Column(name = "bsis_qy")
    private Long bsisQy; // 기초 수량

    @Column(name = "change_qy_acqs")
    private Long changeQyAcqs; // 변동 수량 취득

    @Column(name = "change_qy_dsps")
    private Long changeQyDsps; // 변동 수량 처분

    @Column(name = "change_qy_incnr")
    private Long changeQyIncnr; // 변동 수량 소각

    @Column(name = "trmend_qy")
    private Long trmendQy; // 기말 수량

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