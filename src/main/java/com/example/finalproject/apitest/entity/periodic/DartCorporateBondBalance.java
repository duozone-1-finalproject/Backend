package com.example.finalproject.apitest.entity.periodic;

// 회사채 미상환 잔액
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_corporate_bond_balance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartCorporateBondBalance {

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

    @Column(name = "yy1_below")
    private Long yy1Below; // 1년 이하

    @Column(name = "yy1_excess_yy2_below")
    private Long yy1ExcessYy2Below; // 1년초과 2년이하

    @Column(name = "yy2_excess_yy3_below")
    private Long yy2ExcessYy3Below; // 2년초과 3년이하

    @Column(name = "yy3_excess_yy4_below")
    private Long yy3ExcessYy4Below; // 3년초과 4년이하

    @Column(name = "yy4_excess_yy5_below")
    private Long yy4ExcessYy5Below; // 4년초과 5년이하

    @Column(name = "yy5_excess_yy10_below")
    private Long yy5ExcessYy10Below; // 5년초과 10년이하

    @Column(name = "yy10_excess")
    private Long yy10Excess; // 10년초과

    @Column(name = "sm")
    private Long sm; // 합계

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