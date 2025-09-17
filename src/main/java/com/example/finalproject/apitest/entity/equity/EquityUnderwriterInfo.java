package com.example.finalproject.apitest.entity.equity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dart_equity_underwriter_info")
@Data
public class EquityUnderwriterInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private String actsen;
    private String actnmn;
    private String stksen;
    private Long udtcnt;
    private Long udtamt;
    private Long udtprc;
    private String udtmth;
}