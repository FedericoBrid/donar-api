package com.donar.api.passwordreset.repository;

import com.donar.api.passwordreset.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByTokenAndUsedFalse(String token);
}
