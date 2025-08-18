// 2. 증권의종류 Entity
package com.example.finalproject.apitest.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;



@Entity
@Table(name = "dart_securities_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartSecuritiesInfo {

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

    @Column(name = "stksen", length = 100)
    private String stksen; // 증권의종류

    @Column(name = "stkcnt")
    private Long stkcnt; // 증권수량

    @Column(name = "fv")
    private Long fv; // 액면가액

    @Column(name = "slprc")
    private Long slprc; // 모집(매출)가액

    @Column(name = "slta")
    private Long slta; // 모집(매출)총액

    @Column(name = "slmthn", length = 100)
    private String slmthn; // 모집(매출)방법

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