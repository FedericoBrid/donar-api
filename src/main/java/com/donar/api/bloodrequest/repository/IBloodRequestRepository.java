package com.donar.api.bloodrequest.repository;

import com.donar.api.bloodrequest.entity.BloodRequest;
import com.donar.api.bloodrequest.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    List<BloodRequest> findByStatus(RequestStatus status);
}
