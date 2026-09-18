package com.donar.api.donation.service;

import com.donar.api.donation.entity.Donation;
import com.donar.api.donation.enums.DonationStatus;
import com.donar.api.donation.repository.IDonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final IDonationRepository donationRepository;

    public List<Donation> findByBloodRequestId(Long bloodRequestId) {
        return donationRepository.findByBloodRequest_Id(bloodRequestId);
    }

    public List<Donation> findByUserId(Long userId) {
        return donationRepository.findByUser_Id(userId);
    }

    public boolean hasRegisteredDonation(Long userId) {
        return donationRepository.findByUser_IdAndStatus(userId, DonationStatus.REGISTERED).isPresent();
    }

}
