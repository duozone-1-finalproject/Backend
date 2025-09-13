package com.example.finalproject.dart_viewer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVersion {

    @Id
    @JsonProperty("id")
    private Long id;

    @JsonProperty("corp_code")
    private Long corpCode;

    @JsonProperty("corp_name")
    private Long corpName;

    // user_id
    @JsonProperty("user_id")
    private Long userId;

    // version
    @JsonProperty("version")
    private String version;

    @JsonProperty("version_number")
    private Long versionNumber;

    // sections
    @JsonProperty("section1")
    private String section1;

    @JsonProperty("section2")
    private String section2;

    @JsonProperty("section3")
    private String section3;

    @JsonProperty("section4")
    private String section4;

    @JsonProperty("section5")
    private String section5;

    @JsonProperty("section6")
    private String section6;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_at")
    private String createdAt;

    // modifiedSections (json 타입)
    @JsonProperty("modified_sections")
    private String modifiedSections;


}
