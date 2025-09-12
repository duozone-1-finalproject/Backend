package com.example.finalproject.apitest.entity.periodic;

// 임원 현황
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_executive_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DartExecutiveStatus {

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

    @Column(name = "nm", length = 100)
    private String nm; // 성명

    @Column(name = "sexdstn", length = 10)
    private String sexdstn; // 성별

    @Column(name = "birth_ym", length = 20)
    private String birthYm; // 출생 년월

    @Column(name = "ofcps", length = 200)
    private String ofcps; // 직위

    @Column(name = "rgist_exctv_at", length = 100)
    private String rgistExctvAt; // 등기 임원 여부

    @Column(name = "fte_at", length = 100)
    private String fteAt; // 상근 여부

    @Column(name = "chrg_job", length = 500)
    private String chrgJob; // 담당 업무

    @Column(name = "main_career", length = 1000)
    private String mainCareer; // 주요 경력

    @Column(name = "mxmm_shrhldr_relate", length = 500)
    private String mxmmShrhldrRelate; // 최대 주주 관계

    @Column(name = "hffc_pd", length = 100)
    private String hffcPd; // 재직 기간

    @Column(name = "tenure_end_on")
    private LocalDate tenureEndOn; // 임기 만료 일

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