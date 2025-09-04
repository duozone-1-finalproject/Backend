package com.example.finalproject.apitest.entity.equity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dart_equity_security_type")
@Data
public class EquitySecurityType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private String stksen;
    private Long stkcnt;
    private Long fv;
    private Long slprc;
    private Long slta;
    private String slmthn;
}