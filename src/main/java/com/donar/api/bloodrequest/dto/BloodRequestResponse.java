package com.donar.api.bloodrequest.dto;

import com.donar.api.bloodrequest.enums.RequestStatus;
import com.donar.api.bloodrequest.enums.Urgency;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BloodRequestResponse(
        Long id,

        Long bloodCenterId,
        String bloodCenterName,
        String bloodCenterAddress,
        String bloodCenterPhone,
        String bloodCenterEmail,

        Long bloodTypeId,
        String bloodTypeName,

        Long rhFactorId,
        String rhFactorName,

        Urgency urgency,
        RequestStatus status,

        LocalDate expirationDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}