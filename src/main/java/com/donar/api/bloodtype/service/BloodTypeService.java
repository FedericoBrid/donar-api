package com.donar.api.bloodtype.service;

import com.donar.api.bloodtype.entity.BloodType;
import com.donar.api.bloodtype.repository.IBloodTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodTypeService {

    private final IBloodTypeRepository bloodTypeRepository;

    public List<BloodType> findAll() {
        return bloodTypeRepository.findAll();
    }

    public BloodType findById(Long id) {
        return bloodTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("Blood type not found with id: " + id));
    }
}
