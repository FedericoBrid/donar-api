package com.donar.api.rhfactor.repository;

import com.donar.api.rhfactor.entity.RhFactor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRhFactorRepository extends JpaRepository<RhFactor, Long> {
}
