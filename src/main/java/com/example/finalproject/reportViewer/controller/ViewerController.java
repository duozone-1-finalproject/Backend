package com.example.finalproject.reportViewer.controller;

import com.example.finalproject.reportViewer.dto.*;
import com.example.finalproject.reportViewer.entity.UserVersion;
import com.example.finalproject.reportViewer.service.UserVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/versions")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // React 앱 주소
public class ViewerController {
    private final UserVersionService userVersionService;

    @GetMapping
    public ResponseEntity<Map<String, VersionResponseDto>> getVersions(@RequestParam Long userId) {
        return ResponseEntity.ok(userVersionService.getVersions(userId));
    }

    // 2. 초기 버전 생성 (v0 등)
    @PostMapping
    public ResponseEntity<UserVersion> createVersion(@RequestBody CreateVersionRequestDto request) {
        return ResponseEntity.ok(userVersionService.createVersion(request));
    }

    // 3-1. 편집 버전 생성 또는 갱신
    @PostMapping("/editing")
    public ResponseEntity<UserVersion> saveEditingVersion(@RequestBody SaveEditingVersionRequestDto request) {
        return ResponseEntity.ok(userVersionService.saveEditingVersion(request));
    }

    // 3-2. 편집 버전 일부 수정 (modifiedSections)
    @PatchMapping("/editing")
    public ResponseEntity<UserVersion> updateEditingModified(@RequestBody UpdateModifiedSectionsRequestDto request) throws Exception {
        return ResponseEntity.ok(userVersionService.updateEditingModified(request));
    }

    // 4. 편집 버전 확정 → 새 버전 저장
    @PostMapping("/finalize")
    public ResponseEntity<Map<String, String>> finalizeVersion(@RequestBody FinalizeVersionRequestDto request) {
        String newVersion = userVersionService.finalizeVersion(request).getVersion();
        return ResponseEntity.ok(Map.of(
                "message", newVersion + " 버전이 최종 저장되었습니다.",
                "version", newVersion
        ));
    }

    @DeleteMapping("/editing")
    public ResponseEntity<String> deleteEditingVersion(@RequestBody DeleteEditingRequestDto request) {
        userVersionService.deleteEditingVersion(request);
        return ResponseEntity.ok("편집중인 버전이 삭제되었습니다.");
    }

}
