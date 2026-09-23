package com.donar.api.auth.service;

import com.donar.api.auth.dto.AuthResponse;
import com.donar.api.auth.dto.LoginRequest;
import com.donar.api.user.entity.User;
import com.donar.api.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new RuntimeException("Invalid Email or Password"));
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid Email or Password");
        }

        if (!user.getStatus()){
            throw new RuntimeException("User is deactivated");
        }

        return new AuthResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
