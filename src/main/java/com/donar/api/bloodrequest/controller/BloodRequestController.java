package com.donar.api.bloodrequest.controller;

import com.donar.api.bloodrequest.dto.BloodRequestResponse;
import com.donar.api.bloodrequest.dto.CreateBloodRequest;
import com.donar.api.bloodrequest.dto.UpdateBloodRequest;
import com.donar.api.bloodrequest.entity.BloodRequest;
import com.donar.api.bloodrequest.enums.RequestStatus;
import com.donar.api.bloodrequest.service.BloodRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blood-requests")
@RequiredArgsConstructor
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HEMOADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BloodRequestResponse create(@Valid @RequestBody CreateBloodRequest request) {
        return toResponse(bloodRequestService.create(request));
    }

    @GetMapping
    public List<BloodRequestResponse> findAll(@RequestParam(required = false) RequestStatus status) {
        List<BloodRequest> requests;

        if (status != null) {
            requests = bloodRequestService.findByStatus(status);
        } else {
            requests = bloodRequestService.findAll();
        }

        return requests.stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public BloodRequestResponse findById(@PathVariable Long id) {
        return toResponse(bloodRequestService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HEMOADMIN')")
    public BloodRequestResponse update(@PathVariable Long id, @Valid @RequestBody UpdateBloodRequest request) {
        return toResponse(bloodRequestService.update(id, request));
    }

    private BloodRequestResponse toResponse(BloodRequest bloodRequest) {

        return new BloodRequestResponse(
                bloodRequest.getId(),

                bloodRequest.getBloodCenter().getId(),
                bloodRequest.getBloodCenter().getName(),
                bloodRequest.getBloodCenter().getAddress(),
                bloodRequest.getBloodCenter().getPhone(),
                bloodRequest.getBloodCenter().getEmail(),

                bloodRequest.getBloodType().getId(),
                bloodRequest.getBloodType().getName(),

                bloodRequest.getRhFactor().getId(),
                bloodRequest.getRhFactor().getName(),

                bloodRequest.getUrgency(),
                bloodRequest.getStatus(),

                bloodRequest.getExpirationDate(),
                bloodRequest.getCreatedAt(),
                bloodRequest.getUpdatedAt()
        );
    }
}