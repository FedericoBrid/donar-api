package com.donar.api.user.dto;

import com.donar.api.user.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(

        Long id,

        String firstName,

        String lastName,

        LocalDate birthDate,

        String email,

        Gender gender,

        Long bloodTypeId,

        String bloodTypeName,

        Long rhFactorId,

        String rhFactorName,

        Boolean status,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}