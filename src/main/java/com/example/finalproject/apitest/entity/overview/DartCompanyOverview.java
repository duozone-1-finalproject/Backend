package com.example.finalproject.apitest.entity.overview;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 기업개황
@Entity
@Table(name = "dart_company_overview")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartCompanyOverview {

    @Id
    @Column(name = "corp_code", length = 8, nullable = false)
    private String corpCode; // 고유번호

    @Column(name = "corp_name", length = 200)
    private String corpName; // 정식회사명

    @Column(name = "corp_name_eng", length = 200)
    private String corpNameEng; // 영문회사명

    @Column(name = "stock_name", length = 200)
    private String stockName; // 종목명(상장사) 또는 약식명칭(기타법인)

    @Column(name = "stock_code", length = 6)
    private String stockCode; // 상장사일 경우 주식종목코드

    @Column(name = "ceo_nm", length = 100)
    private String ceoNm; // 대표자명

    @Column(name = "corp_cls", length = 1)
    private String corpCls; // 법인구분 (Y:유가, K:코스닥, N:코넥스, E:기타)

    @Column(name = "jurir_no", length = 13)
    private String jurirNo; // 법인등록번호

    @Column(name = "bizr_no", length = 10)
    private String bizrNo; // 사업자등록번호

    @Column(name = "adres", length = 500)
    private String adres; // 주소

    @Column(name = "hm_url", length = 200)
    private String hmUrl; // 홈페이지

    @Column(name = "ir_url", length = 200)
    private String irUrl; // IR홈페이지

    @Column(name = "phn_no", length = 50)
    private String phnNo; // 전화번호

    @Column(name = "fax_no", length = 50)
    private String faxNo; // 팩스번호

    @Column(name = "induty_code", length = 10)
    private String indutyCode; // 업종코드

    @Column(name = "est_dt")
    private LocalDate estDt; // 설립일 (YYYYMMDD)

    @Column(name = "acc_mt", length = 2)
    private String accMt; // 결산월 (MM)

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
