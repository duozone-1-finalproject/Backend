package com.example.finalproject.reportViewer.service.impl;

import com.example.finalproject.reportViewer.dto.*;
import com.example.finalproject.reportViewer.entity.UserVersion;
import com.example.finalproject.reportViewer.repository.UserVersionRepository;
import com.example.finalproject.reportViewer.service.UserVersionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.finalproject.reportViewer.constant.VersionConstant.SECTION_FIELDS;

@Service
@RequiredArgsConstructor
public class UserVersionServiceImpl implements UserVersionService {

    private final UserVersionRepository userVersionRepository;
    private final RestClient fastApiClient;

    private String getSection(UserVersion entity, String field) {
        return switch (field) {
            case "section1" -> entity.getSection1();
            case "section2" -> entity.getSection2();
            case "section3" -> entity.getSection3();
            case "section4" -> entity.getSection4();
            case "section5" -> entity.getSection5();
            case "section6" -> entity.getSection6();
            default -> null;
        };
    }

    private void setSection(UserVersion entity, String field, String value) {
        switch (field) {
            case "section1" -> entity.setSection1(value);
            case "section2" -> entity.setSection2(value);
            case "section3" -> entity.setSection3(value);
            case "section4" -> entity.setSection4(value);
            case "section5" -> entity.setSection5(value);
            case "section6" -> entity.setSection6(value);
        }
    }

    @Override
    // 1. 모든 버전 조회
    public Map<String, VersionResponseDto> getVersions(Long userId) throws IOException {
        return userVersionRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(
                        UserVersion::getVersion,
                        v -> new VersionResponseDto(
                                v.getSection1(),
                                v.getSection2(),
                                v.getSection3(),
                                v.getSection4(),
                                v.getSection5(),
                                v.getSection6(),
                                v.getDescription(),
                                v.getCreatedAt(),
                                v.getModifiedSections()
                        ),
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public UserVersion createVersion(CreateVersionRequestDto request) throws IOException {
        UserVersion newEntry = new UserVersion();
        newEntry.setUserId(request.getUserId());
        newEntry.setVersion(request.getVersion());
        newEntry.setVersionNumber(request.getVersionNumber());
        newEntry.setDescription(request.getDescription());
        newEntry.setCreatedAt(request.getCreatedAt());

        // section1~6 채우기
        Map<String, String> sections = request.getSectionsData();
        newEntry.setSection1(sections.get("section1"));
        newEntry.setSection2(sections.get("section2"));
        newEntry.setSection3(sections.get("section3"));
        newEntry.setSection4(sections.get("section4"));
        newEntry.setSection5(sections.get("section5"));
        newEntry.setSection6(sections.get("section6"));

        return userVersionRepository.save(newEntry); // DB 저장 + 엔티티 반환
    }

    @Override
    public UserVersion saveEditingVersion(SaveEditingVersionRequestDto request) throws IOException {
        UserVersion editing = userVersionRepository.findByUserIdAndVersion(request.getUserId(), "editing")
                .orElseGet(() -> {
                    UserVersion u = new UserVersion();
                    u.setVersion("editing");
                    u.setUserId(request.getUserId());
                    return u;
                });

        // 마지막 버전 데이터 가져오기 (신규 생성 시만)
        if (editing.getId() == null) {
            userVersionRepository.findTopByUserIdAndVersionNotOrderByIdDesc(request.getUserId(), "editing")
                    .ifPresent(last -> SECTION_FIELDS.forEach(f -> setSection(editing, f, getSection(last, f))));
        }

        // request 섹션 데이터로 덮어쓰기
        if (request.getSectionsData() != null) {
            request.getSectionsData().forEach((k,v) -> setSection(editing, k, v));
        }

        // 공통 필드 업데이트
        editing.setDescription(request.getDescription());
        editing.setCreatedAt(request.getCreatedAt());

        return userVersionRepository.save(editing);
    }


    @Override
    public UserVersion updateEditingModified(UpdateModifiedSectionsRequestDto request) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        UserVersion editing = userVersionRepository.findByUserIdAndVersion(request.getUserId(), "editing")
                .orElseThrow(() -> new RuntimeException("편집중인 버전이 없습니다."));

        // List<String> → JSON 문자열
        String json = mapper.writeValueAsString(request.getModifiedSections());
        editing.setModifiedSections(json);

        return userVersionRepository.save(editing);
    }

    @Override
    public UserVersion finalizeVersion(FinalizeVersionRequestDto request) throws IOException {
        UserVersion editing = userVersionRepository.findByUserIdAndVersion(request.getUserId(), "editing")
                .orElseThrow(() -> new RuntimeException("편집중인 버전이 없습니다."));

        // 현재 이 코드에서 계속 v0를 리턴하고 있음 -> 그래서 계속 v1만 업데이트 되는 현상 발생.
        Optional<UserVersion> lastOpt = userVersionRepository.findTopByUserIdAndVersionNotOrderByIdDesc(request.getUserId(), "editing");

        int newNum = 0;
        if (lastOpt.isPresent() && lastOpt.get().getVersion().startsWith("v")) {
            newNum = Integer.parseInt(lastOpt.get().getVersion().replace("v", "")) + 1;
        }
        String newVersion = "v" + newNum;

        UserVersion newEntry = UserVersion.builder()
                .userId(request.getUserId())
                .version(newVersion)
                .versionNumber((long) newNum)
                .description(request.getDescription())
                .createdAt(request.getCreatedAt())
                .section1(editing.getSection1())
                .section2(editing.getSection2())
                .section3(editing.getSection3())
                .section4(editing.getSection4())
                .section5(editing.getSection5())
                .section6(editing.getSection6())
                .build();
        userVersionRepository.delete(request.getUserId());

        return userVersionRepository.save(newEntry);
    }

    @Override
    @Transactional
    public void deleteEditingVersion(DeleteEditingRequestDto request) throws IOException {
        userVersionRepository.delete(request.getUserId());
    }
}
