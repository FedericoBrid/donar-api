package com.donar.api.user.dto;

import com.donar.api.user.enums.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateUserRequest(

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must contain at least 8 characters")
        String password,

        @NotNull(message = "Gender is required")
        Gender gender,

        @NotNull(message = "Blood type is required")
        Long bloodTypeId,

        @NotNull(message = "Rh factor is required")
        Long rhFactorId
) {
}