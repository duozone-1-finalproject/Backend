package com.example.finalproject.apitest.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;

// 3. 인수인정보 Entity
@Entity
@Table(name = "dart_underwriter_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartUnderwriterInfo {

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

    @Column(name = "actsen", length = 100)
    private String actsen; // 인수인구분

    @Column(name = "actnmn", length = 200)
    private String actnmn; // 인수인명

    @Column(name = "stksen", length = 100)
    private String stksen; // 증권의종류

    @Column(name = "udtcnt")
    private Long udtcnt; // 인수수량

    @Column(name = "udtamt")
    private Long udtamt; // 인수금액

    @Column(name = "udtprc")
    private Long udtprc; // 인수대가

    @Column(name = "udtmth", length = 100)
    private String udtmth; // 인수방법

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