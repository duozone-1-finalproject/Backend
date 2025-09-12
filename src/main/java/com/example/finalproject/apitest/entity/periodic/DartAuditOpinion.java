package com.example.finalproject.apitest.entity.periodic;

// 회계감사인의 명칭 및 감사의견
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_audit_opinion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartAuditOpinion {

    @Id
    @Column(name = "rcept_no", length = 14, nullable = false)
    private String rceptNo; // 접수번호

    @Column(name = "corp_cls", length = 1)
    private String corpCls; // 법인구분

    @Column(name = "corp_code", length = 8)
    private String corpCode; // 고유번호

    @Column(name = "corp_name", length = 200)
    private String corpName; // 회사명

    @Column(name = "bsns_year", length = 20)
    private String bsnsYear; // 사업연도

    @Column(name = "adtor", length = 200)
    private String adtor; // 감사인

    @Column(name = "adt_opinion", columnDefinition = "TEXT")
    private String adtOpinion; // 감사의견

    // --- 2019년 12월 8일 이전 필드 ---
    @Column(name = "adt_reprt_spcmnt_matter", columnDefinition = "TEXT")
    private String adtReprtSpcmntMatter; // 감사보고서 특기사항

    // --- 2019년 12월 9일 이후 필드 ---
    @Column(name = "emphs_matter", columnDefinition = "TEXT")
    private String emphsMatter; // 강조사항 등

    @Column(name = "core_adt_matter", columnDefinition = "TEXT")
    private String coreAdtMatter; // 핵심감사사항

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