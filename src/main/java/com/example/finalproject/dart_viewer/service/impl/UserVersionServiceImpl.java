package com.example.finalproject.dart_viewer.service.impl;

import com.example.finalproject.dart_viewer.dto.*;
import com.example.finalproject.dart_viewer.entity.UserVersion;
import com.example.finalproject.dart_viewer.repository.UserVersionRepository;
import com.example.finalproject.dart_viewer.service.UserVersionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.example.finalproject.dart_viewer.constant.VersionConstant.SECTION_FIELDS;

@Service
@Slf4j
public class UserVersionServiceImpl implements UserVersionService {

    private final UserVersionRepository userVersionRepository;
    private final RestClient fastApiClient;

    public UserVersionServiceImpl(
            UserVersionRepository userVersionRepository,
            @Qualifier("fastApiClient") RestClient fastApiClient
    ) {
        this.userVersionRepository = userVersionRepository;
        this.fastApiClient = fastApiClient;
    }

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
    public List<CompanyInfoDto> getUserCompanies(Long userId) throws IOException {
        // 사용자가 작성중인 모든 회사 정보 조회 (중복 제거)
        return userVersionRepository.findByUserId(userId)
                .stream()
                .collect(Collectors.toMap(
                        UserVersion::getCorpCode,
                        v -> new CompanyInfoDto(v.getCorpCode(), v.getCompanyName()),
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public CompanyVersionsDto getCompanyVersions(GetCompanyVersionsRequestDto request) throws IOException {
        Map<String, VersionResponseDto> versions = userVersionRepository.findByUserIdAndCorpCode(request.getUserId(), request.getCorpCode()).stream()
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

        // 회사명은 첫 번째 버전에서 가져오거나 request에서 가져옴
        String companyName = userVersionRepository.findByUserIdAndCorpCode(request.getUserId(), request.getCorpCode())
                .stream()
                .findFirst()
                .map(UserVersion::getCompanyName)
                .orElse(null);

        return new CompanyVersionsDto(request.getCorpCode(), companyName, versions);
    }

    @Override
    public UserVersion createVersion(CreateVersionRequestDto request) throws IOException {
        UserVersion newEntry = new UserVersion();
        newEntry.setUserId(request.getUserId());
        newEntry.setCorpCode(request.getCorpCode());
        newEntry.setCompanyName(request.getCompanyName());
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
        AtomicBoolean isNew = new AtomicBoolean(false);

        UserVersion editing = userVersionRepository.findByUserIdAndCorpCodeAndVersion(request.getUserId(), request.getCorpCode(),"editing")
                .orElseGet(() -> {
                    isNew.set(true);
                    UserVersion u = new UserVersion();
                    u.setVersion("editing");
                    u.setUserId(request.getUserId());
                    u.setCorpCode(request.getCorpCode());
                    u.setCompanyName(request.getCompanyName());
                    return u;
                });

        // 마지막 버전 데이터 가져오기 (신규 생성 시만)
        if (isNew.get()) {
            userVersionRepository.findTopByUserIdAndCorpCodeAndVersionNotOrderByIdDesc(request.getUserId(), request.getCorpCode(), "editing")
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

        UserVersion editing = userVersionRepository.findByUserIdAndCorpCodeAndVersion(request.getUserId(), request.getCorpCode(), "editing")
                .orElseThrow(() -> new RuntimeException("편집중인 버전이 없습니다."));

        // List<String> → JSON 문자열
        String json = mapper.writeValueAsString(request.getModifiedSections());
        editing.setModifiedSections(json);

        return userVersionRepository.save(editing);
    }

    @Override
    public UserVersion finalizeVersion(FinalizeVersionRequestDto request) throws IOException {
        UserVersion editing = userVersionRepository.findByUserIdAndCorpCodeAndVersion(request.getUserId(), request.getCorpCode(), "editing")
                .orElseThrow(() -> new RuntimeException("편집중인 버전이 없습니다."));

        // 현재 이 코드에서 계속 v0를 리턴하고 있음 -> 그래서 계속 v1만 업데이트 되는 현상 발생.
        Optional<UserVersion> lastOpt = userVersionRepository.findTopByUserIdAndCorpCodeAndVersionNotOrderByIdDesc(request.getUserId(), request.getCorpCode(), "editing");

        int newNum = 0;
        if (lastOpt.isPresent() && lastOpt.get().getVersion().startsWith("v")) {
            newNum = Integer.parseInt(lastOpt.get().getVersion().replace("v", "")) + 1;
        }
        String newVersion = "v" + newNum;

        UserVersion newEntry = UserVersion.builder()
                .userId(request.getUserId())
                .corpCode(request.getCorpCode())
                .companyName(editing.getCompanyName())
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
        userVersionRepository.deleteVersion(request.getUserId(), request.getCorpCode(), "editing");

        return userVersionRepository.save(newEntry);
    }

    @Override
    @Transactional
    public void deleteVersion(DeleteVersionRequestDto request) throws IOException {
        // 특정 버전이 존재하는지 확인
        Optional<UserVersion> version = userVersionRepository.findByUserIdAndCorpCodeAndVersion(
                request.getUserId(), request.getCorpCode(), request.getVersion());

        if (version.isEmpty()) {
            throw new RuntimeException("삭제할 버전이 존재하지 않습니다.");
        }

        userVersionRepository.deleteVersion(request.getUserId(), request.getCorpCode(), request.getVersion());
    }

    @Override
    @Transactional
    public void deleteCompany(DeleteCompanyRequestDto request) throws IOException {
        // 해당 회사의 버전이 존재하는지 확인
        List<UserVersion> versions = userVersionRepository.findByUserIdAndCorpCode(
                request.getUserId(), request.getCorpCode());

        if (versions.isEmpty()) {
            throw new RuntimeException("삭제할 회사 데이터가 존재하지 않습니다.");
        }

        userVersionRepository.deleteCompany(request.getUserId(), request.getCorpCode());
    }
}
