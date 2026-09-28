package com.donar.api.donation.dto;

import com.donar.api.bloodrequest.enums.RequestStatus;
import com.donar.api.bloodrequest.enums.Urgency;
import com.donar.api.donation.enums.DonationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MyDonationResponse(
        Long donationId,

        Long bloodRequestId,

        String bloodTypeName,
        String rhFactorName,

        Urgency urgency,
        RequestStatus requestStatus,

        DonationStatus donationStatus,

        LocalDate expirationDate,

        Long bloodCenterId,
        String bloodCenterName,
        String bloodCenterAddress,
        String bloodCenterPhone,
        String bloodCenterEmail,

        LocalDateTime registeredAt,
        LocalDateTime donatedAt,
        LocalDateTime updatedAt
) {}