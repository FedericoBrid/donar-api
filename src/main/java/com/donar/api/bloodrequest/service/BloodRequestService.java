package com.donar.api.bloodrequest.service;

import com.donar.api.bloodrequest.entity.BloodRequest;
import com.donar.api.bloodrequest.enums.RequestStatus;
import com.donar.api.bloodrequest.repository.IBloodRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodRequestService {

    private final IBloodRequestRepository bloodRequestRepository;

    public List<BloodRequest> findAll() {
        return bloodRequestRepository.findAll();
    }

    public BloodRequest findById(Long id) {
        return bloodRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Blood request not found"));
    }

    public List<BloodRequest> findByStatus(RequestStatus status) {
        return bloodRequestRepository.findByStatus(status);
    }
}
