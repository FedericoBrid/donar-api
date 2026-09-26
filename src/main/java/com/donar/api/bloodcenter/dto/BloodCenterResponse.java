package com.donar.api.bloodcenter.dto;

import java.time.LocalDateTime;

public record BloodCenterResponse(
        Long id,
        String name,
        String address,
        String phone,
        String email,
        Boolean isPublic,
        Boolean status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}