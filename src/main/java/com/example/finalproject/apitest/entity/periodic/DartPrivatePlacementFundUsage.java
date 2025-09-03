package com.example.finalproject.apitest.entity.periodic;

// 사모자금의 사용내역
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_private_placement_fund_usage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartPrivatePlacementFundUsage {

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

    @Column(name = "se_nm", length = 200)
    private String seNm; // 구분

    @Column(name = "tm", length = 20)
    private String tm; // 회차

    @Column(name = "pay_de")
    private LocalDate payDe; // 납입일

    // --- 2018년 1월 18일 이전 필드 ---
    @Column(name = "pay_amount")
    private Long payAmount; // 납입금액

    @Column(name = "cptal_use_plan", columnDefinition = "TEXT")
    private String cptalUsePlan; // 자금사용 계획

    @Column(name = "real_cptal_use_sttus", columnDefinition = "TEXT")
    private String realCptalUseSttus; // 실제 자금사용 현황

    // --- 2018년 1월 19일 이후 필드 ---
    @Column(name = "mtrpt_cptal_use_plan_useprps", columnDefinition = "TEXT")
    private String mtrptCptalUsePlanUseprps; // 주요사항보고서의 자금사용 계획(사용용도)

    @Column(name = "mtrpt_cptal_use_plan_picure_amount")
    private Long mtrptCptalUsePlanPicureAmount; // 주요사항보고서의 자금사용 계획(조달금액)

    @Column(name = "real_cptal_use_dtls_cn", columnDefinition = "TEXT")
    private String realCptalUseDtlsCn; // 실제 자금사용 내역(내용)

    @Column(name = "real_cptal_use_dtls_amount")
    private Long realCptalUseDtlsAmount; // 실제 자금사용 내역(금액)

    @Column(name = "dffrnc_occrrnc_resn", columnDefinition = "TEXT")
    private String dffrncOccrrncResn; // 차이발생 사유 등

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