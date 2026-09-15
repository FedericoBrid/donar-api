package com.donar.api.bloodtype.repository;

import com.donar.api.bloodtype.entity.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBloodTypeRepository extends JpaRepository<BloodType, Long> {
}
