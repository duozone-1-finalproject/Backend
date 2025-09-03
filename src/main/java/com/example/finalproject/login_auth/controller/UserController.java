// UserController.java
package com.example.finalproject.login_auth.controller;

import com.example.finalproject.login_auth.dto.UserDto; // UserDto import
import com.example.finalproject.login_auth.model.User;
import com.example.finalproject.login_auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/check")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        boolean exists = userService.checkUsernameDuplication(username);
        return ResponseEntity.ok(!exists); // 존재하지 않으면 true 반환
    }


    @GetMapping("/me")
    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // JWT 토큰에서 추출된 Email
        log.info("인증된 사용자 이름 (UserController): {}", username);

        // UserService를 통해 User 조회 로직 위임
        User user = userService.findUserByUsername(username);

        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}