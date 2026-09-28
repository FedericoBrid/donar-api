package com.donar.api.donation.dto;

import com.donar.api.donation.enums.DonationStatus;

import java.time.LocalDateTime;

public record DonationResponse(
        Long id,
        Long userId,
        Long bloodRequestId,
        DonationStatus status,
        LocalDateTime registeredAt,
        LocalDateTime donatedAt,
        LocalDateTime updatedAt
) {}