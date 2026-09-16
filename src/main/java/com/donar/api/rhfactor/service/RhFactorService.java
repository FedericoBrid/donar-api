package com.donar.api.rhfactor.service;

import com.donar.api.rhfactor.entity.RhFactor;
import com.donar.api.rhfactor.repository.IRhFactorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RhFactorService {

    private final IRhFactorRepository rhFactorRepository;

    public List<RhFactor> findAll() {
        return rhFactorRepository.findAll();
    }

    public RhFactor findById(Long id) {
        return rhFactorRepository.findById(id).orElseThrow(() -> new RuntimeException("Rh factor not found with id: " + id));
    }
}
