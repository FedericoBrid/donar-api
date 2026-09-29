package com.donar.api.donation.controller;

import com.donar.api.bloodcenter.entity.BloodCenter;
import com.donar.api.bloodrequest.entity.BloodRequest;
import com.donar.api.donation.dto.CreateDonationRequest;
import com.donar.api.donation.dto.DonationResponse;
import com.donar.api.donation.dto.MyDonationResponse;
import com.donar.api.donation.entity.Donation;
import com.donar.api.donation.service.DonationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/donations")
@SecurityRequirement(name = "bearerAuth")
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

    @GetMapping("/my")
    public List<MyDonationResponse> findMyDonations(Authentication authentication) {
        Long userId = (Long) authentication.getDetails();

        return donationService.findByUserId(userId)
                .stream()
                .map(this::toMyDonationResponse)
                .toList();
    }

    @GetMapping("/blood-request/{bloodRequestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HEMOADMIN')")
    public List<DonationResponse> findByBloodRequest(@PathVariable Long bloodRequestId) {
        return donationService.findByBloodRequestId(bloodRequestId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MyDonationResponse toMyDonationResponse(Donation donation) {

        BloodRequest bloodRequest = donation.getBloodRequest();
        BloodCenter bloodCenter = bloodRequest.getBloodCenter();

        return new MyDonationResponse(
                donation.getId(),
                bloodRequest.getId(),

                bloodRequest.getBloodType().getName(),
                bloodRequest.getRhFactor().getName(),

                bloodRequest.getUrgency(),
                bloodRequest.getStatus(),

                donation.getStatus(),

                bloodRequest.getExpirationDate(),

                bloodCenter.getId(),
                bloodCenter.getName(),
                bloodCenter.getAddress(),
                bloodCenter.getPhone(),
                bloodCenter.getEmail(),

                donation.getRegisteredAt(),
                donation.getDonatedAt(),
                donation.getUpdatedAt()
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
