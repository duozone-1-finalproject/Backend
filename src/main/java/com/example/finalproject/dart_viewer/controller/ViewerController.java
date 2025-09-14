package com.example.finalproject.dart_viewer.controller;

import com.example.finalproject.dart_viewer.dto.*;
import com.example.finalproject.dart_viewer.entity.UserVersion;
import com.example.finalproject.dart_viewer.service.UserVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/versions")
@CrossOrigin(origins = "${frontend.url}", allowCredentials = "true")
public class ViewerController {

    private final UserVersionService userVersionService;

    @GetMapping("/companies")
    public ResponseEntity<List<CompanyInfoDto>> getUserCompanies(@RequestParam Long userId) throws IOException {
        return ResponseEntity.ok(userVersionService.getUserCompanies(userId));
    }

    @PostMapping("/search")
    public ResponseEntity<CompanyVersionsDto> getCompanyVersions(@RequestBody GetCompanyVersionsRequestDto request) throws IOException {
        return ResponseEntity.ok(userVersionService.getCompanyVersions(request));
    }

    // 2. 초기 버전 생성 (v0 등)
    @PostMapping
    public ResponseEntity<UserVersion> createVersion(@RequestBody CreateVersionRequestDto request) throws IOException {
        return ResponseEntity.ok(userVersionService.createVersion(request));
    }

    // 3-1. 편집 버전 생성 또는 갱신
    @PostMapping("/editing")
    public ResponseEntity<UserVersion> saveEditingVersion(@RequestBody SaveEditingVersionRequestDto request) throws IOException {
        return ResponseEntity.ok(userVersionService.saveEditingVersion(request));
    }

    // 3-2. 편집 버전 일부 수정 (modifiedSections)
    @PatchMapping("/editing")
    public ResponseEntity<UserVersion> updateEditingModified(@RequestBody UpdateModifiedSectionsRequestDto request) throws Exception {
        return ResponseEntity.ok(userVersionService.updateEditingModified(request));
    }

    // 4. 편집 버전 확정 → 새 버전 저장
    @PostMapping("/finalize")
    public ResponseEntity<Map<String, String>> finalizeVersion(@RequestBody FinalizeVersionRequestDto request) throws IOException {
        String newVersion = userVersionService.finalizeVersion(request).getVersion();
        return ResponseEntity.ok(Map.of(
                "message", newVersion + " 버전이 최종 저장되었습니다.",
                "version", newVersion
        ));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteVersion(@RequestBody DeleteVersionRequestDto request) throws IOException {
        userVersionService.deleteVersion(request);
        return ResponseEntity.ok("버전이 삭제되었습니다.");
    }

    @DeleteMapping("/company")
    public ResponseEntity<String> deleteCompany(@RequestBody DeleteCompanyRequestDto request) throws IOException {
        userVersionService.deleteCompany(request);
        return ResponseEntity.ok("회사의 모든 증권신고서가 삭제되었습니다.");
    }

}
