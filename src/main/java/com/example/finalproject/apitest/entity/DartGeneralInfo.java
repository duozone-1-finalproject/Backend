// 1. 일반사항 Entity
package com.example.finalproject.apitest.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "dart_general_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartGeneralInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rcept_no", length = 14, nullable = false)
    private String rceptNo;

    @Column(name = "corp_cls", length = 1)
    private String corpCls;

    @Column(name = "corp_code", length = 8)
    private String corpCode;

    @Column(name = "corp_name", length = 200)
    private String corpName;

    @Column(name = "sbd")
    private LocalDate sbd; // 청약기일

    @Column(name = "pymd")
    private LocalDate pymd; // 납입기일

    @Column(name = "sband")
    private LocalDate sband; // 청약공고일

    @Column(name = "asand")
    private LocalDate asand; // 배정공고일

    @Column(name = "asstd")
    private LocalDate asstd; // 배정기준일

    @Column(name = "exstk", columnDefinition = "TEXT")
    private String exstk; // 신주인수권 행사대상증권

    @Column(name = "exprc")
    private Long exprc; // 신주인수권 행사가격

    @Column(name = "expd", columnDefinition = "TEXT")
    private String expd; // 신주인수권 행사기간

    @Column(name = "rpt_rcpn", length = 14)
    private String rptRcpn; // 주요사항보고서 접수번호

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