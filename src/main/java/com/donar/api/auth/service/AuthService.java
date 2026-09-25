package com.donar.api.auth.service;

import com.donar.api.auth.dto.AuthResponse;
import com.donar.api.auth.dto.LoginRequest;
import com.donar.api.common.exception.InactiveUserException;
import com.donar.api.common.exception.InvalidCredentialsException;
import com.donar.api.security.service.JwtService;
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
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (!user.getStatus()){
            throw new InactiveUserException("User is inactive");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail()
        );

        return new AuthResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), token);
    }
}
