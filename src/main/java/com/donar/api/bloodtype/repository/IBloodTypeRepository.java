package com.donar.api.bloodtype.repository;

import com.donar.api.bloodtype.entity.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IBloodTypeRepository extends JpaRepository<BloodType, Long> {
    boolean existsByName(String name);

    Optional<BloodType> findByName(String name);
}
