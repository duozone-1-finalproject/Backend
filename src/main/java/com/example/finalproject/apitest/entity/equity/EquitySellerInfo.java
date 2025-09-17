package com.example.finalproject.apitest.entity.equity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dart_equity_seller_info")
@Data
public class EquitySellerInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String rceptNo;
    private String corpCls;
    private String corpCode;
    private String corpName;
    private String hdr;
    private String rlCmp;
    private Long bfslHdstk;
    private Long slstk;
    private Long atslHdstk;
}