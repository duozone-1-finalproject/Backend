package com.example.finalproject.reportViewer.service;

import com.example.finalproject.reportViewer.dto.*;
import com.example.finalproject.reportViewer.entity.UserVersion;

import java.util.Map;

public interface UserVersionService {

    Map<String, VersionResponseDto> getVersions(Long userId);
    UserVersion createVersion(CreateVersionRequestDto request);
    UserVersion saveEditingVersion(SaveEditingVersionRequestDto request);
    UserVersion updateEditingModified(UpdateModifiedSectionsRequestDto request) throws Exception;
    UserVersion finalizeVersion(FinalizeVersionRequestDto request);
    void deleteEditingVersion(DeleteEditingRequestDto request);

}
