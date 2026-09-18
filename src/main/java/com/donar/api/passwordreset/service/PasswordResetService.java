package com.donar.api.passwordreset.service;

import com.donar.api.passwordreset.entity.PasswordReset;
import com.donar.api.passwordreset.repository.IPasswordResetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final IPasswordResetRepository passwordResetRepository;

    public PasswordReset findValidToken(String token) {
        PasswordReset passwordReset = passwordResetRepository
                .findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if (passwordReset.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset token has expired");
        }

        return passwordReset;
    }
}
