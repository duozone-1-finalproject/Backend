package com.example.finalproject.apitest.entity.equity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dart_equity_general_info")
@Data
@NoArgsConstructor
public class EquityGeneralInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private String sbd;
    private LocalDate pymd;
    private LocalDate sband;
    private LocalDate asand;
    private LocalDate asstd;
    private String exstk;
    private Long exprc;
    private String expd;
    private String rptRcpn;

    private LocalDateTime createdAt;
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
