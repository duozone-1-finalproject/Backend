package com.example.finalproject.dart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "induty_table")
@Data
@NoArgsConstructor
public class IndutyTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String indutyCode;

    @Column
    private String indutyName;
}
