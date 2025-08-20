package com.example.finalproject.reportViewer.repository;

import com.example.finalproject.reportViewer.entity.UserVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVersionRepository extends JpaRepository<UserVersion, Long> {

    List<UserVersion> findByUserId(Long userId);

    Optional<UserVersion> findByUserIdAndVersion(Long userId, String version);

    Optional<UserVersion> findTopByUserIdAndVersionNotOrderByIdDesc(Long userId, String version);
}
