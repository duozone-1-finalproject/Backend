package com.example.finalproject.apitest.entity.periodic;

// 감사용역체결현황
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_audit_service_contract")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartAuditServiceContract {

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

    @Column(name = "adtor", length = 200)
    private String adtor; // 감사인

    @Column(name = "cn", length = 1000)
    private String cn; // 내용

    // --- 2020년 7월 5일 이전 필드 ---
    @Column(name = "mendng")
    private String mendng; // 보수

    @Column(name = "tot_reqre_time")
    private String totReqreTime; // 총소요시간

    // --- 2020년 7월 6일 이후 필드 ---
    @Column(name = "adt_cntrct_dtls_mendng")
    private String adtCntrctDtlsMendng; // 감사계약내역(보수)

    @Column(name = "adt_cntrct_dtls_time")
    private String adtCntrctDtlsTime; // 감사계약내역(시간)

    @Column(name = "real_exc_dtls_mendng")
    private String realExcDtlsMendng; // 실제수행내역(보수)

    @Column(name = "real_exc_dtls_time")
    private String realExcDtlsTime; // 실제수행내역(시간)

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