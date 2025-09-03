// File: entity/equity/EquityGeneralInfo.java
package com.example.finalproject.apitest.entity.equity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "dart_equity_general_info")
@Data
public class EquityGeneralInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(unique = true) private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private LocalDate sbd;
    private LocalDate pymd;
    private LocalDate sband;
    private LocalDate asand;
    private LocalDate asstd;
    private String exstk;
    private Long exprc;
    private String expd;
    private String rptRcpn;
}