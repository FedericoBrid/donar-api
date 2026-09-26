package com.donar.api.bloodcenter.service;

import com.donar.api.bloodcenter.dto.CreateBloodCenterRequest;
import com.donar.api.bloodcenter.dto.UpdateBloodCenterRequest;
import com.donar.api.bloodcenter.entity.BloodCenter;
import com.donar.api.bloodcenter.repository.IBloodCenterRepository;
import com.donar.api.common.exception.DuplicateResourceException;
import com.donar.api.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodCenterService {

    private final IBloodCenterRepository bloodCenterRepository;

    public List<BloodCenter> findAll() {
        return bloodCenterRepository.findAll();
    }

    public BloodCenter findById(Long id) {
        return bloodCenterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blood Center not found with id: " + id));
    }

    @Transactional
    public BloodCenter create(CreateBloodCenterRequest request) {
        if (bloodCenterRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException(
                    "Blood center email already exists"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        BloodCenter bloodCenter = new BloodCenter();
        bloodCenter.setName(request.name());
        bloodCenter.setAddress(request.address());
        bloodCenter.setPhone(request.phone());
        bloodCenter.setEmail(request.email());
        bloodCenter.setIsPublic(request.isPublic());
        bloodCenter.setStatus(true);
        bloodCenter.setCreatedAt(now);

        return bloodCenterRepository.save(bloodCenter);
    }

    @Transactional
    public BloodCenter update(Long id, UpdateBloodCenterRequest request) {
        BloodCenter bloodCenter = bloodCenterRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Blood center not found"
                )
        );

        if (!bloodCenter.getEmail().equals(request.email())
                && bloodCenterRepository.existsByEmail(request.email())) {

            throw new DuplicateResourceException(
                    "Blood center email already exists"
            );
        }

        bloodCenter.setName(request.name());
        bloodCenter.setAddress(request.address());
        bloodCenter.setPhone(request.phone());
        bloodCenter.setEmail(request.email());
        bloodCenter.setIsPublic(request.isPublic());
        bloodCenter.setUpdatedAt(LocalDateTime.now());

        return bloodCenterRepository.save(bloodCenter);
    }

    @Transactional
    public BloodCenter deactivate(Long id){
        BloodCenter bloodCenter = bloodCenterRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Blood center not found"
                )
        );
        bloodCenter.setStatus(false);
        bloodCenter.setUpdatedAt(LocalDateTime.now());

        return bloodCenterRepository.save(bloodCenter);
    }

    @Transactional
    public BloodCenter activate(Long id) {

        BloodCenter bloodCenter = bloodCenterRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Blood center not found"
                        )
                );

        bloodCenter.setStatus(true);
        bloodCenter.setUpdatedAt(LocalDateTime.now());

        return bloodCenterRepository.save(bloodCenter);
    }
}
