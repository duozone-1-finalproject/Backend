// UserService.java
package com.example.test_02.login_auth.service;

import com.example.test_02.login_auth.dto.UserRequestDto;
import com.example.test_02.login_auth.model.User;
import com.example.test_02.login_auth.repository.UserRepository;
import com.example.test_02.login_auth.util.PasswordValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
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
                .name(requestDto.getEmail()) // 이름은 따로 받지만, 여기서는 이메일로 저장 (UserRequestDto에서 name을 받으므로 requestDto.getName() 사용 가능)
                .provider("local") // 소셜 로그인과 구분
                .role("USER") // 기본 역할 설정
                .build();
        System.out.println("🔔 회원가입 실행 직전: ");
        userRepository.save(user);
        System.out.println("👉 저장 직전 유저: " + user);
    }

    public boolean checkUsernameDuplication(String username) {
        return userRepository.existsByUsername(username);
    }

    // ✅ OAuth2 로그인 사용자를 처리하는 메서드 (기존 기능 유지)
    @Transactional
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

    // ✅ 사용자 이름으로 User 객체를 찾는 메서드 (UserInfoController에서 사용)
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));
    }
}