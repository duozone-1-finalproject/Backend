package com.example.test_02.login_auth.service;

import com.example.test_02.login_auth.model.User;
import com.example.test_02.login_auth.repository.UserRepository;
import com.example.test_02.login_auth.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + username));

        return UserPrincipal.create(user);
    }
}
