package com.example.finalproject.apitest.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;

// 6. 일반청약자환매청구권 Entity
@Entity
@Table(name = "dart_redemption_rights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartRedemptionRights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_title", length = 100)
    private String groupTitle;

    @Column(name = "rcept_no", length = 14, nullable = false)
    private String rceptNo;

    @Column(name = "corp_cls", length = 1)
    private String corpCls;

    @Column(name = "corp_code", length = 8)
    private String corpCode;

    @Column(name = "corp_name", length = 200)
    private String corpName;

    @Column(name = "grtrs", length = 200)
    private String grtrs; // 부여사유

    @Column(name = "exavivr", length = 200)
    private String exavivr; // 행사가능 투자자

    @Column(name = "grtcnt")
    private Long grtcnt; // 부여수량

    @Column(name = "expd", columnDefinition = "TEXT")
    private String expd; // 행사기간

    @Column(name = "exprc")
    private Long exprc; // 행사가격

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