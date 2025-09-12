package com.example.finalproject.dart.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "corp_code")
@Data
@NoArgsConstructor
public class CorpCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String corpCode;

    @Column
    private String corpName;

    @Column
    private String corpEngName;

    @Column(nullable = true)
    private String stockCode;

    @Column
    private String modifyDate;
}