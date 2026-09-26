package com.donar.api.bloodcenter.controller;

import com.donar.api.bloodcenter.dto.BloodCenterResponse;
import com.donar.api.bloodcenter.dto.CreateBloodCenterRequest;
import com.donar.api.bloodcenter.dto.UpdateBloodCenterRequest;
import com.donar.api.bloodcenter.entity.BloodCenter;
import com.donar.api.bloodcenter.service.BloodCenterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blood-centers")
@RequiredArgsConstructor
public class BloodCenterController {

    private final BloodCenterService bloodCenterService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HEMOADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BloodCenterResponse create(
            @Valid @RequestBody CreateBloodCenterRequest request
    ) {
        BloodCenter bloodCenter =
                bloodCenterService.create(request);

        return toResponse(bloodCenter);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HEMOADMIN')")
    public List<BloodCenterResponse> findAll() {
        return bloodCenterService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HEMOADMIN')")
    public BloodCenterResponse findById(
            @PathVariable Long id
    ) {
        BloodCenter bloodCenter =
                bloodCenterService.findById(id);

        return toResponse(bloodCenter);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HEMOADMIN')")
    public BloodCenterResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBloodCenterRequest request
    ) {
        BloodCenter bloodCenter =
                bloodCenterService.update(id, request);

        return toResponse(bloodCenter);
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'HEMOADMIN')")
    public BloodCenterResponse activate(
            @PathVariable Long id
    ) {
        BloodCenter bloodCenter =
                bloodCenterService.activate(id);

        return toResponse(bloodCenter);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'HEMOADMIN')")
    public BloodCenterResponse deactivate(
            @PathVariable Long id
    ) {
        BloodCenter bloodCenter =
                bloodCenterService.deactivate(id);

        return toResponse(bloodCenter);
    }

    private BloodCenterResponse toResponse(BloodCenter bloodCenter) {

        return new BloodCenterResponse(
                bloodCenter.getId(),
                bloodCenter.getName(),
                bloodCenter.getAddress(),
                bloodCenter.getPhone(),
                bloodCenter.getEmail(),
                bloodCenter.getIsPublic(),
                bloodCenter.getStatus(),
                bloodCenter.getCreatedAt(),
                bloodCenter.getUpdatedAt()
        );
    }
}
