// UserRepository.java
package com.example.test_02.repository;

import com.example.test_02.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 로컬 로그인용
    Optional<User> findByUsername(String username);

    // 이메일 + provider 조합으로 소셜 로그인 유저 조회
    Optional<User> findByEmailAndProvider(String email, String provider);

    // 이메일만으로도 조회 (중복 체크나 공통처리용)
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    // 인증된 사용자 이름으로 전체 정보 조회 (필요한 경우)
    Optional<User> findByEmailOrUsername(String email, String username);
}