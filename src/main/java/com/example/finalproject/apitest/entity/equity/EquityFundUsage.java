package com.example.finalproject.apitest.entity.equity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dart_equity_fund_usage")
@Data
public class EquityFundUsage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private String se;
    private Long amt;
}