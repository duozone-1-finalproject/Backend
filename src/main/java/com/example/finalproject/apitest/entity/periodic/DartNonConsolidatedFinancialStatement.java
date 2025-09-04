package com.example.finalproject.apitest.entity.periodic;

// 단일회사 전체 재무제표
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "dart_non_consolidated_fs") // table name is shortened for convenience
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartNonConsolidatedFinancialStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rcept_no", length = 14, nullable = false)
    private String rceptNo; // 접수번호

    @Column(name = "reprt_code", length = 5)
    private String reprtCode; // 보고서 코드

    @Column(name = "bsns_year", length = 4)
    private String bsnsYear; // 사업 연도

    @Column(name = "corp_code", length = 8)
    private String corpCode; // 고유번호

    @Column(name = "sj_div", length = 10)
    private String sjDiv; // 재무제표구분 (BS, IS, CIS, CF, SCE)

    @Column(name = "sj_nm", length = 200)
    private String sjNm; // 재무제표명

    @Column(name = "account_id", length = 100)
    private String accountId; // 계정ID

    @Column(name = "account_nm", length = 200)
    private String accountNm; // 계정명

    @Column(name = "account_detail", length = 200)
    private String accountDetail; // 계정상세

    @Column(name = "thstrm_nm", length = 100)
    private String thstrmNm; // 당기명

    @Column(name = "thstrm_amount")
    private Long thstrmAmount; // 당기금액

    @Column(name = "thstrm_add_amount")
    private Long thstrmAddAmount; // 당기누적금액

    @Column(name = "frmtrm_nm", length = 100)
    private String frmtrmNm; // 전기명

    @Column(name = "frmtrm_amount")
    private Long frmtrmAmount; // 전기금액

    @Column(name = "frmtrm_q_nm", length = 100)
    private String frmtrmQNm; // 전기명(분/반기)

    @Column(name = "frmtrm_q_amount")
    private Long frmtrmQAmount; // 전기금액(분/반기)

    @Column(name = "frmtrm_add_amount")
    private Long frmtrmAddAmount; // 전기누적금액

    @Column(name = "bfefrmtrm_nm", length = 100)
    private String bfefrmtrmNm; // 전전기명

    @Column(name = "bfefrmtrm_amount")
    private Long bfefrmtrmAmount; // 전전기금액

    @Column(name = "ord")
    private Integer ord; // 계정과목 정렬순서

    @Column(name = "currency", length = 10)
    private String currency; // 통화 단위

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