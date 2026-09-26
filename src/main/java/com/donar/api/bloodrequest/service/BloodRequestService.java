package com.donar.api.bloodrequest.service;

import com.donar.api.bloodcenter.entity.BloodCenter;
import com.donar.api.bloodcenter.repository.IBloodCenterRepository;
import com.donar.api.bloodrequest.dto.CreateBloodRequest;
import com.donar.api.bloodrequest.dto.UpdateBloodRequest;
import com.donar.api.bloodrequest.entity.BloodRequest;
import com.donar.api.bloodrequest.enums.RequestStatus;
import com.donar.api.bloodrequest.repository.IBloodRequestRepository;
import com.donar.api.bloodtype.entity.BloodType;
import com.donar.api.bloodtype.repository.IBloodTypeRepository;
import com.donar.api.common.exception.ResourceNotFoundException;
import com.donar.api.rhfactor.entity.RhFactor;
import com.donar.api.rhfactor.repository.IRhFactorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodRequestService {

    private final IBloodRequestRepository bloodRequestRepository;
    private final IBloodCenterRepository bloodCenterRepository;
    private final IBloodTypeRepository bloodTypeRepository;
    private final IRhFactorRepository rhFactorRepository;

    public List<BloodRequest> findAll() {
        return bloodRequestRepository.findAll();
    }

    public BloodRequest findById(Long id) {
        return bloodRequestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blood request not found"));
    }

    public List<BloodRequest> findByStatus(RequestStatus status) {
        return bloodRequestRepository.findByStatus(status);
    }

    @Transactional
    public BloodRequest create(CreateBloodRequest request) {

        BloodCenter bloodCenter =
                bloodCenterRepository.findById(request.bloodCenterId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Blood center not found"
                                )
                        );

        BloodType bloodType =
                bloodTypeRepository.findById(request.bloodTypeId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Blood type not found"
                                )
                        );

        RhFactor rhFactor =
                rhFactorRepository.findById(request.rhFactorId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Rh factor not found"
                                )
                        );

        LocalDateTime now = LocalDateTime.now();

        BloodRequest bloodRequest = new BloodRequest();

        bloodRequest.setBloodCenter(bloodCenter);
        bloodRequest.setBloodType(bloodType);
        bloodRequest.setRhFactor(rhFactor);
        bloodRequest.setUrgency(request.urgency());
        bloodRequest.setExpirationDate(request.expirationDate());

        bloodRequest.setStatus(RequestStatus.ACTIVE);
        bloodRequest.setCreatedAt(now);

        return bloodRequestRepository.save(bloodRequest);
    }

    @Transactional
    public BloodRequest update(Long id, UpdateBloodRequest request) {

        BloodRequest bloodRequest =
                bloodRequestRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Blood request not found"
                                )
                        );

        BloodCenter bloodCenter =
                bloodCenterRepository.findById(request.bloodCenterId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Blood center not found"
                                )
                        );

        BloodType bloodType =
                bloodTypeRepository.findById(request.bloodTypeId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Blood type not found"
                                )
                        );

        RhFactor rhFactor =
                rhFactorRepository.findById(request.rhFactorId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Rh factor not found"
                                )
                        );

        bloodRequest.setBloodCenter(bloodCenter);
        bloodRequest.setBloodType(bloodType);
        bloodRequest.setRhFactor(rhFactor);
        bloodRequest.setUrgency(request.urgency());
        bloodRequest.setExpirationDate(request.expirationDate());
        bloodRequest.setUpdatedAt(LocalDateTime.now());

        return bloodRequestRepository.save(bloodRequest);
    }
}
