package com.example.finalproject.login_auth.service;

import com.example.finalproject.login_auth.dto.UserRequestDto;
import com.example.finalproject.login_auth.entity.User;
import com.example.finalproject.login_auth.repository.UserRepository;
import com.example.finalproject.login_auth.util.PasswordValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // @Transactional 제거: Elasticsearch는 ACID 트랜잭션을 지원하지 않음
    public void register(UserRequestDto requestDto) {
        // 아이디 중복 검사
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 유효성 검사
        if (!PasswordValidator.isValid(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이며, 대문자, 소문자, 숫자를 모두 포함해야 합니다.");
        }

        // User 엔티티 생성 및 저장
        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .name(requestDto.getName()) // requestDto에서 name 사용
                .provider("local") // 소셜 로그인과 구분
                .role("USER") // 기본 역할 설정
                .build();

        log.info("🔔 회원가입 실행 직전: ");
        userRepository.save(user);
        log.info("👉 저장 완료 유저: {}", user.getUsername());
    }

    public boolean checkUsernameDuplication(String username) {
        return userRepository.existsByUsername(username);
    }

    // OAuth2 로그인 사용자를 처리하는 메서드 (기존 기능 유지)
    // @Transactional 제거
    public User processOAuthUser(String email, String name, String provider) {
        // 이메일로 사용자 조회 (username과 email이 unique이므로 email로 조회하는 것이 안전)
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .username(email) // Google 로그인 시 username을 email로 설정
                    .name(name)
                    .provider(provider)
                    .role("USER") // 기본 역할 설정
                    .build();
            return userRepository.save(newUser);
        });
    }

    // 사용자 이름으로 User 객체를 찾는 메서드 (UserInfoController에서 사용)
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));
    }

    // 리프레시 토큰 관련 메서드들 추가
    public void saveRefreshToken(String username, String refreshToken) {
        User user = findUserByUsername(username);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        log.info("리프레시 토큰 저장 완료: {}", username);
    }

    public void deleteRefreshToken(String username) {
        User user = findUserByUsername(username);
        user.setRefreshToken(null);
        user.setRefreshTokenExpiryDate(null);
        userRepository.save(user);
        log.info("리프레시 토큰 삭제 완료: {}", username);
    }

    public User findUserByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .orElse(null);
    }
}