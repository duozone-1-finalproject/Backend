package com.example.apitest.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;

// 5. 매출인에관한사항 Entity
@Entity
@Table(name = "dart_seller_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartSellerInfo {

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

    @Column(name = "hdr", length = 200)
    private String hdr; // 보유자

    @Column(name = "rl_cmp", length = 200)
    private String rlCmp; // 회사와의관계

    @Column(name = "bfsl_hdstk")
    private Long bfslHdstk; // 매출전보유증권수

    @Column(name = "slstk")
    private Long slstk; // 매출증권수

    @Column(name = "atsl_hdstk")
    private Long atslHdstk; // 매출후보유증권수

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