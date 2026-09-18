package com.donar.api.user.dto;

import com.donar.api.user.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdateUserRequest(

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        @NotNull(message = "Gender is required")
        Gender gender,

        @NotNull(message = "Blood type is required")
        Long bloodTypeId,

        @NotNull(message = "Rh factor is required")
        Long rhFactorId
) {
}
