package com.donar.api.bloodcenter.repository;

import com.donar.api.bloodcenter.entity.BloodCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBloodCenterRepository extends JpaRepository<BloodCenter, Long> {
    boolean existsByEmail(String email);
}
