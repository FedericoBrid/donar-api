package com.donar.api.donation.service;

import com.donar.api.bloodrequest.entity.BloodRequest;
import com.donar.api.bloodrequest.enums.RequestStatus;
import com.donar.api.bloodrequest.repository.IBloodRequestRepository;
import com.donar.api.common.exception.DuplicateResourceException;
import com.donar.api.common.exception.ResourceNotFoundException;
import com.donar.api.donation.dto.CreateDonationRequest;
import com.donar.api.donation.entity.Donation;
import com.donar.api.donation.enums.DonationStatus;
import com.donar.api.donation.repository.IDonationRepository;
import com.donar.api.user.entity.User;
import com.donar.api.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final IDonationRepository donationRepository;
    private final IUserRepository userRepository;
    private final IBloodRequestRepository bloodRequestRepository;

    public List<Donation> findByBloodRequestId(Long bloodRequestId) {
        return donationRepository.findByBloodRequest_Id(bloodRequestId);
    }

    public List<Donation> findByUserId(Long userId) {
        return donationRepository.findByUser_Id(userId);
    }

    public boolean hasRegisteredDonation(Long userId) {
        return donationRepository.findByUser_IdAndStatus(userId, DonationStatus.REGISTERED).isPresent();
    }

    @Transactional
    public Donation create(Long userId, CreateDonationRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BloodRequest bloodRequest = bloodRequestRepository.findById(request.bloodRequestId())
                        .orElseThrow(() -> new ResourceNotFoundException("Blood request not found"));

        if (bloodRequest.getStatus() != RequestStatus.ACTIVE) {
            throw new IllegalStateException("Blood request is not active");
        }

        if (hasRegisteredDonation(userId)) {
            throw new DuplicateResourceException("User already has a registered donation");
        }

        LocalDateTime now = LocalDateTime.now();

        Donation donation = new Donation();

        donation.setUser(user);
        donation.setBloodRequest(bloodRequest);
        donation.setStatus(DonationStatus.REGISTERED);
        donation.setRegisteredAt(now);

        return donationRepository.save(donation);
    }

}
