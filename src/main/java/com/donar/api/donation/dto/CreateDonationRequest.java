package com.donar.api.donation.dto;

import jakarta.validation.constraints.NotNull;

public record CreateDonationRequest(
        @NotNull
        Long bloodRequestId
) {}