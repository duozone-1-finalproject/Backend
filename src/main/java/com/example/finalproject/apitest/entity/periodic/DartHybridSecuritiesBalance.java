package com.example.finalproject.apitest.entity.periodic;

// 신종자본증권 미상환 잔액
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_hybrid_securities_balance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartHybridSecuritiesBalance {

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

    @Column(name = "yy1_excess_yy5_below")
    private Long yy1ExcessYy5Below; // 1년초과 5년이하

    @Column(name = "yy5_excess_yy10_below")
    private Long yy5ExcessYy10Below; // 5년초과 10년이하

    @Column(name = "yy10_excess_yy15_below")
    private Long yy10ExcessYy15Below; // 10년초과 15년이하

    @Column(name = "yy15_excess_yy20_below")
    private Long yy15ExcessYy20Below; // 15년초과 20년이하

    @Column(name = "yy20_excess_yy30_below")
    private Long yy20ExcessYy30Below; // 20년초과 30년이하

    @Column(name = "yy30_excess")
    private Long yy30Excess; // 30년초과

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