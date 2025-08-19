package com.example.finalproject.apitest.entity.periodic;

// 회계감사인과의 비감사용역 계약체결 현황
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_non_audit_service_contract")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartAuditorNonAuditContract {

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

    @Column(name = "bsns_year", length = 20)
    private String bsnsYear; // 사업연도

    @Column(name = "cntrct_cncls_de")
    private LocalDate cntrctCnclsDe; // 계약체결일

    @Column(name = "servc_cn", length = 1000)
    private String servcCn; // 용역내용

    @Column(name = "servc_exc_pd", length = 200)
    private String servcExcPd; // 용역수행기간

    @Column(name = "servc_mendng", length = 200)
    private String servcMendng; // 용역보수

    @Column(name = "rm", length = 500)
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