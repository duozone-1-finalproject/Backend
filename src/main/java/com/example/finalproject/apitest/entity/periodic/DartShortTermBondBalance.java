package com.example.finalproject.apitest.entity.periodic;

// 단기사채 미상환 잔액
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_short_term_bond_balance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartShortTermBondBalance {

    @Id
    @Column(name = "rcept_no", length = 14, nullable = false)
    private String rceptNo; // 접수번호

    @Column(name = "corp_cls", length = 1)
    private String corpCls; // 법인구분

    @Column(name = "corp_code", length = 8)
    private String corpCode; // 고유번호

    @Column(name = "corp_name", length = 200)
    private String corpName; // 회사명

    @Column(name = "remndr_exprtn1", length = 200)
    private String remndrExprtn1; // 잔여만기1

    @Column(name = "remndr_exprtn2", length = 200)
    private String remndrExprtn2; // 잔여만기2

    @Column(name = "de10_below")
    private Long de10Below; // 10일 이하

    @Column(name = "de10_excess_de30_below")
    private Long de10ExcessDe30Below; // 10일초과 30일이하

    @Column(name = "de30_excess_de90_below")
    private Long de30ExcessDe90Below; // 30일초과 90일이하

    @Column(name = "de90_excess_de180_below")
    private Long de90ExcessDe180Below; // 90일초과 180일이하

    @Column(name = "de180_excess_yy1_below")
    private Long de180ExcessYy1Below; // 180일초과 1년이하

    @Column(name = "sm")
    private Long sm; // 합계

    @Column(name = "isu_lmt")
    private Long isuLmt; // 발행 한도

    @Column(name = "remndr_lmt")
    private Long remndrLmt; // 잔여 한도

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