package com.example.finalproject.reportViewer.service;

import com.example.finalproject.reportViewer.dto.*;
import com.example.finalproject.reportViewer.entity.UserVersion;

import java.io.IOException;
import java.util.Map;

public interface UserVersionService {

    Map<String, VersionResponseDto> getVersions(Long userId) throws IOException;
    UserVersion createVersion(CreateVersionRequestDto request) throws IOException;
    UserVersion saveEditingVersion(SaveEditingVersionRequestDto request) throws IOException;
    UserVersion updateEditingModified(UpdateModifiedSectionsRequestDto request) throws Exception;
    UserVersion finalizeVersion(FinalizeVersionRequestDto request) throws IOException;
    void deleteEditingVersion(DeleteEditingRequestDto request) throws IOException;

}
