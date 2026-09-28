package com.donar.api.donation.controller;

import com.donar.api.donation.dto.CreateDonationRequest;
import com.donar.api.donation.dto.DonationResponse;
import com.donar.api.donation.entity.Donation;
import com.donar.api.donation.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DonationResponse create(
            @Valid @RequestBody CreateDonationRequest request,
            Authentication authentication) {

        Long userId = (Long) authentication.getDetails();
        return toResponse(donationService.create(userId, request));
    }

    @PatchMapping("/{id}/cancel")
    public DonationResponse cancel(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = (Long) authentication.getDetails();

        return toResponse(
                donationService.cancel(id, userId)
        );
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'HEMOADMIN')")
    public DonationResponse complete(@PathVariable Long id) {

        return toResponse(
                donationService.complete(id)
        );
    }

    private DonationResponse toResponse(Donation donation) {

        return new DonationResponse(
                donation.getId(),
                donation.getUser().getId(),
                donation.getBloodRequest().getId(),
                donation.getStatus(),
                donation.getRegisteredAt(),
                donation.getDonatedAt(),
                donation.getUpdatedAt()
        );
    }
}
