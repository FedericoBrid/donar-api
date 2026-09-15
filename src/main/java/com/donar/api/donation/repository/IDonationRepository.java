package com.donar.api.donation.repository;

import com.donar.api.donation.entity.Donation;
import com.donar.api.donation.enums.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IDonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByBloodRequest_Id(Long bloodRequestId);

    List<Donation> findByUser_Id(Long userId);
    Optional<Donation> findByUser_IdAndStatus(Long userId, DonationStatus status);
}
