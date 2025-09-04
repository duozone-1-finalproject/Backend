package com.example.finalproject.apitest.entity.equity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dart_equity_repurchase_option")
@Data
public class EquityRepurchaseOption {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private String grtrs;
    private String exavivr;
    private Long grtcnt;
    private String expd;
    private Long exprc;
}
