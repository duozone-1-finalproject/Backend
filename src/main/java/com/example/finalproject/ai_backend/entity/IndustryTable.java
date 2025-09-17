// src/main/java/com/example/finalproject/ai_backend/entity/IndustryTable.java
package com.example.finalproject.ai_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Table(name = "induty_table")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndustryTable {

    @Id
    @Column(name = "induty_code", nullable = false)
    private String indutyCode;

    @Column(name = "induty_name")
    private String indutyName;
}
