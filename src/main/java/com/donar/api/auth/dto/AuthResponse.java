package com.donar.api.auth.dto;

public record AuthResponse(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String token
) {
}
