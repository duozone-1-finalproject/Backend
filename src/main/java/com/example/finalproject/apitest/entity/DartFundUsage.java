package com.example.apitest.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;

// 4. 자금사용목적 Entity
@Entity
@Table(name = "dart_fund_usage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartFundUsage {

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

    @Column(name = "se", length = 200)
    private String se; // 구분

    @Column(name = "amt")
    private Long amt; // 금액

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