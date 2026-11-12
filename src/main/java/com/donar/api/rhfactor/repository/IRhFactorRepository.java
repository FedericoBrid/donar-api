package com.donar.api.rhfactor.repository;

import com.donar.api.rhfactor.entity.RhFactor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRhFactorRepository extends JpaRepository<RhFactor, Long> {
    boolean existsByName(String name);

    Optional<RhFactor> findByName(String name);
}
