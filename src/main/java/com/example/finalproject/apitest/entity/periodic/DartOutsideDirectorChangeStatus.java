package com.example.finalproject.apitest.entity.periodic;

// 사외이사 및 그 변동현황
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_outside_director_change_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DartOutsideDirectorChangeStatus {

    @Id
    @Column(name = "rcept_no", length = 14, nullable = false)
    private String rceptNo; // 접수번호

    @Column(name = "corp_cls", length = 1)
    private String corpCls; // 법인구분

    @Column(name = "corp_code", length = 8)
    private String corpCode; // 고유번호

    @Column(name = "corp_name", length = 200)
    private String corpName; // 회사명

    @Column(name = "drctr_co")
    private Long drctrCo; // 이사의 수

    @Column(name = "otcmp_drctr_co")
    private Long otcmpDrctrCo; // 사외이사 수

    @Column(name = "apnt")
    private Long apnt; // 사외이사 변동현황(선임)

    @Column(name = "rlsofc")
    private Long rlsofc; // 사외이사 변동현황(해임)

    @Column(name = "mdstrm_resig")
    private Long mdstrmResig; // 사외이사 변동현황(중도퇴임)

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