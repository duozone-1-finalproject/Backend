package com.example.finalproject.apitest.entity;

// 이사·감사 전체의 보수현황(보수지급금액 - 유형별)

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_director_and_auditor_compensation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartDirectorAndAuditorCompensation {
    @Id
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

    @Column(name = "nmpr")
    private Long nmpr; // 인원수

    @Column(name = "pymnt_totamt")
    private Long pymntTotamt; // 보수총액

    @Column(name = "psn1_avrg_pymntamt")
    private Long psn1AvrgPymntamt; // 1인당 평균보수액

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