package com.example.test_02.dart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company_overview")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyOverview {

    @Id
    @Column(name = "corp_code", length = 8, nullable = false)
    private String corpCode;

    @Column(name = "corp_name", length = 100, nullable = false)
    private String corpName;

    @Column(name = "corp_cls", length = 1)
    private String corpCls;

    @Column(name = "adres", length = 255)
    private String adres;

    @Column(name = "hm_url", length = 200)
    private String hmUrl;

    @Column(name = "induty_code", length = 10)
    private String indutyCode;

    @Column(name = "induty_name", length = 100)
    private String indutyName;

    @Column(name = "est_dt", length = 8)
    private String estDt;

    @Column(name = "favorite_count")
    private Integer favoriteCount;

    @Column(name = "logo", length = 300)
    private String logo;
}
