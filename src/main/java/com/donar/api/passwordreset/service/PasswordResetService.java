package com.donar.api.passwordreset.service;

import com.donar.api.common.exception.PasswordResetException;
import com.donar.api.passwordreset.entity.PasswordReset;
import com.donar.api.passwordreset.repository.IPasswordResetRepository;
import com.donar.api.user.entity.User;
import com.donar.api.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final IPasswordResetRepository passwordResetRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return;
        }

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        PasswordReset passwordReset = new PasswordReset();

        passwordReset.setUser(user);
        passwordReset.setToken(token);
        passwordReset.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        passwordReset.setUsed(false);
        passwordReset.setCreatedAt(LocalDateTime.now());

        passwordResetRepository.save(passwordReset);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {

        PasswordReset passwordReset = findValidToken(token);

        User user = passwordReset.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        passwordReset.setUsed(true);

        passwordResetRepository.save(passwordReset);
    }


    public PasswordReset findValidToken(String token) {
        PasswordReset passwordReset = passwordResetRepository
                .findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new PasswordResetException("Invalid password reset token"));

        if (!passwordReset.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new PasswordResetException("Password reset token has expired");
        }

        return passwordReset;
    }
}
