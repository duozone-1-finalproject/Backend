package com.example.finalproject.login_auth.service;

import com.example.finalproject.login_auth.dto.UserRequestDto;
import com.example.finalproject.login_auth.entity.User;
import com.example.finalproject.login_auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void register(UserRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다.");
        }

        User newUser = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword()) // 암호화 필요
                .role("USER")
                .provider(requestDto.getProvider())  // <- 여기 수정
                .name(requestDto.getName())
                .build();

        userRepository.save(newUser);
    }

    // 아이디 중복 체크 메서드 추가
    public boolean checkUsernameDuplication(String username) {
        return userRepository.existsByUsername(username);
    }

    // 사용자 조회 메서드 추가
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}
