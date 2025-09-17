package com.example.finalproject.apitest.entity.overview;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Table(name = "company_overview")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyOverview2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "corp_code", nullable = false, length = 8)
    private String corpCode;          // 고유번호(8자리)

    @Column(name = "corp_name", length = 200)
    private String corpName;          // 정식명칭

    @Column(name = "corp_name_eng", length = 200)
    private String corpNameEng;       // 영문명칭

    @Column(name = "stock_name", length = 100)
    private String stockName;         // 종목명(상장사) 또는 약식명칭(기타법인)

    @Column(name = "stock_code", length = 6)
    private String stockCode;         // 상장회사의 종목코드(6자리)

    @Column(name = "ceo_nm", length = 100)
    private String ceoNm;             // 대표자명

    @Column(name = "corp_cls", length = 1)
    private String corpCls;           // 법인구분 : Y(유가), K(코스닥), N(코넥스), E(기타)

    @Column(name = "jurir_no", length = 13)
    private String jurirNo;           // 법인등록번호

    @Column(name = "bizr_no", length = 10)
    private String bizrNo;            // 사업자등록번호

    @Column(name = "adres", length = 500)
    private String adres;             // 주소

    @Column(name = "hm_url", length = 200)
    private String hmUrl;             // 홈페이지

    @Column(name = "ir_url", length = 200)
    private String irUrl;             // IR홈페이지

    @Column(name = "phn_no", length = 20)
    private String phnNo;             // 전화번호

    @Column(name = "fax_no", length = 20)
    private String faxNo;             // 팩스번호

    @Column(name = "induty_code", length = 10)
    private String indutyCode;        // 업종코드

    @Column(name = "est_dt", length = 8)
    private String estDt;             // 설립일(YYYYMMDD)

    @Column(name = "acc_mt", length = 2)
    private String accMt;             // 결산월(MM)

    @Column(name = "created_at")
    private LocalDateTime createdAt;  // 생성일시

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // 수정일시

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 편의 메서드: 법인구분 코드를 한글로 변환
    public String getCorpClsName() {
        if (corpCls == null) return "기타";

        switch (corpCls) {
            case "Y": return "유가증권시장";
            case "K": return "코스닥";
            case "N": return "코넥스";
            case "E": return "기타";
            default: return "기타";
        }
    }

    // 편의 메서드: 설립일 포맷팅 (YYYY-MM-DD)
    public String getFormattedEstDt() {
        if (estDt == null || estDt.length() != 8) return estDt;

        return estDt.substring(0, 4) + "-" +
                estDt.substring(4, 6) + "-" +
                estDt.substring(6, 8);
    }
}