package com.donar.api.passwordreset.service;

import com.donar.api.common.exception.PasswordResetException;
import com.donar.api.email.service.EmailService;
import com.donar.api.passwordreset.entity.PasswordReset;
import com.donar.api.passwordreset.repository.IPasswordResetRepository;
import com.donar.api.user.entity.User;
import com.donar.api.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final IPasswordResetRepository passwordResetRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) return;

        // Invalidar solicitudes de recuperación anteriores.
        List<PasswordReset> previousRequests =
                passwordResetRepository.findAllByUser_IdAndUsedFalse(user.getId());

        previousRequests.forEach(request -> request.setUsed(true));

        passwordResetRepository.saveAll(previousRequests);

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        // Token original que recibirá el usuario por correo.
        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        // Hash que se almacenará en la base de datos.
        String tokenHash = hashToken(token);

        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setUser(user);
        passwordReset.setTokenHash(tokenHash);
        passwordReset.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        passwordReset.setUsed(false);
        passwordReset.setCreatedAt(LocalDateTime.now());

        passwordResetRepository.save(passwordReset);

        // Enviamos el token original, nunca el hash.
        emailService.sendPasswordResetEmail(user.getEmail(), token);
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

    @Transactional(readOnly = true)
    public PasswordReset findValidToken(String token) {

        String tokenHash = hashToken(token);

        PasswordReset passwordReset = passwordResetRepository
                .findByTokenHashAndUsedFalse(tokenHash)
                .orElseThrow(() ->
                        new PasswordResetException("Invalid password reset token"));

        if (!passwordReset.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new PasswordResetException("Password reset token has expired");
        }

        return passwordReset;
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
