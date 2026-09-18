package com.donar.api.bloodcenter.service;

import com.donar.api.bloodcenter.entity.BloodCenter;
import com.donar.api.bloodcenter.repository.IBloodCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodCenterService {

    private final IBloodCenterRepository bloodCenterRepository;

    public List<BloodCenter> findAll() {
        return bloodCenterRepository.findAll();
    }

    public BloodCenter findById(Long id) {
        return bloodCenterRepository.findById(id).orElseThrow(() -> new RuntimeException("Blood Center not found with id: " + id));
    }
}
