package com.donar.api.bloodrequest.dto;

import com.donar.api.bloodrequest.enums.Urgency;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateBloodRequest(

        @NotNull
        Long bloodCenterId,

        @NotNull
        Long bloodTypeId,

        @NotNull
        Long rhFactorId,

        @NotNull
        Urgency urgency,

        @NotNull
        @Future
        LocalDate expirationDate
) {}