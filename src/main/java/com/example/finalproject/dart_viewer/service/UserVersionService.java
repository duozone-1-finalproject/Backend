package com.example.finalproject.dart_viewer.service;

import com.example.finalproject.dart_viewer.dto.*;
import com.example.finalproject.dart_viewer.entity.UserVersion;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserVersionService {

    List<CompanyInfoDto> getUserCompanies(Long userId) throws IOException;
    CompanyVersionsDto getCompanyVersions(GetCompanyVersionsRequestDto request) throws IOException;
    UserVersion createVersion(CreateVersionRequestDto request) throws IOException;
    UserVersion saveEditingVersion(SaveEditingVersionRequestDto request) throws IOException;
    UserVersion updateEditingModified(UpdateModifiedSectionsRequestDto request) throws Exception;
    UserVersion finalizeVersion(FinalizeVersionRequestDto request) throws IOException;
    void deleteVersion(DeleteVersionRequestDto request) throws IOException;
    void deleteCompany(DeleteCompanyRequestDto request) throws IOException;

}
