package com.example.finalproject.reportViewer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_versions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user_id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // version
    @Column(length = 100)
    private String version;

    // sections
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String section1;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String section2;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String section3;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String section4;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String section5;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String section6;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String createdAt;

    // modifiedSections (json 타입)
    @Column(columnDefinition = "JSON")
    private String modifiedSections;

}
