package com.example.finalproject.apitest.entity.periodic;

// 직원 현황
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_employee_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartEmployeeStatus {

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

    @Column(name = "fo_bbm", length = 200)
    private String foBbm; // 사업부문

    @Column(name = "sexdstn", length = 10)
    private String sexdstn; // 성별

    @Column(name = "reform_bfe_emp_co_rgllb")
    private Long reformBfeEmpCoRgllb; // 개정 전 직원 수 정규직

    @Column(name = "reform_bfe_emp_co_cnttk")
    private Long reformBfeEmpCoCnttk; // 개정 전 직원 수 계약직

    @Column(name = "reform_bfe_emp_co_etc")
    private Long reformBfeEmpCoEtc; // 개정 전 직원 수 기타

    @Column(name = "rgllbr_co")
    private Long rgllbrCo; // 정규직 근로자 수

    @Column(name = "rgllbr_abacpt_labrr_co")
    private Long rgllbrAbacptLabrrCo; // 정규직 단시간 근로자 수

    @Column(name = "cnttk_co")
    private Long cnttkCo; // 계약직 근로자 수

    @Column(name = "cnttk_abacpt_labrr_co")
    private Long cnttkAbacptLabrrCo; // 계약직 단시간 근로자 수

    @Column(name = "sm")
    private Long sm; // 합계

    @Column(name = "avrg_cnwk_sdytrn")
    private String avrgCnwkSdytrn; // 평균 근속 연수

    @Column(name = "fyer_salary_totamt")
    private Long fyerSalaryTotamt; // 연간 급여 총액

    @Column(name = "jan_salary_am")
    private Long janSalaryAm; // 1인 평균 급여액

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