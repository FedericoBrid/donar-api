package com.donar.api.bloodcenter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBloodCenterRequest(

        @NotBlank
        String name,

        @NotBlank
        String address,

        @NotBlank
        String phone,

        @NotBlank
        @Email
        String email,

        @NotNull
        Boolean isPublic
) {}