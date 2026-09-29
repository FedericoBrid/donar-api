package com.donar.api.donation.service;

import com.donar.api.bloodrequest.entity.BloodRequest;
import com.donar.api.bloodrequest.enums.RequestStatus;
import com.donar.api.bloodrequest.repository.IBloodRequestRepository;
import com.donar.api.common.exception.DuplicateResourceException;
import com.donar.api.common.exception.InvalidStateException;
import com.donar.api.common.exception.ResourceNotFoundException;
import com.donar.api.donation.dto.CreateDonationRequest;
import com.donar.api.donation.entity.Donation;
import com.donar.api.donation.enums.DonationStatus;
import com.donar.api.donation.repository.IDonationRepository;
import com.donar.api.user.entity.User;
import com.donar.api.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final IDonationRepository donationRepository;
    private final IUserRepository userRepository;
    private final IBloodRequestRepository bloodRequestRepository;

    @Value("${donation.recovery-days}")
    private long recoveryDays;

    @Transactional(readOnly = true)
    public List<Donation> findByUserId(Long userId) {
        return donationRepository.findByUser_Id(userId);
    }

    @Transactional(readOnly = true)
    public List<Donation> findByBloodRequestId(Long bloodRequestId) {

        if (!bloodRequestRepository.existsById(bloodRequestId)) {throw new ResourceNotFoundException("Blood request not found");
        }

        return donationRepository.findByBloodRequest_Id(bloodRequestId);
    }

    @Transactional
    public Donation create(
            Long userId,
            CreateDonationRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BloodRequest bloodRequest =
                bloodRequestRepository.findById(request.bloodRequestId())
                        .orElseThrow(() -> new ResourceNotFoundException("Blood request not found"));

        /*
         * A donation can only be registered
         * for an active blood request.
         */
        if (bloodRequest.getStatus() != RequestStatus.ACTIVE) {
            throw new InvalidStateException("Blood request is not active");
        }

        /*
         * A user can only have one active
         * donation registration at a time.
         */
        if (hasRegisteredDonation(userId)) {
            throw new DuplicateResourceException("User already has a registered donation");
        }

        /*
         * After completing a donation,
         * the user must wait for the recovery period.
         */
        if (!isEligibleToDonate(userId)) {
            throw new InvalidStateException("User is not eligible to donate yet");
        }

        LocalDateTime now = LocalDateTime.now();

        Donation donation = new Donation();

        donation.setUser(user);
        donation.setBloodRequest(bloodRequest);
        donation.setStatus(DonationStatus.REGISTERED);
        donation.setRegisteredAt(now);
        donation.setUpdatedAt(now);

        return donationRepository.save(donation);
    }

    @Transactional
    public Donation cancel(
            Long donationId,
            Long userId) {

        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));

        /*
         * A user can only cancel their own donation.
         */
        if (!donation.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Donation not found");
        }

        /*
         * Only a registered donation can be cancelled.
         */
        if (donation.getStatus() != DonationStatus.REGISTERED) {
            throw new InvalidStateException("Donation cannot be cancelled");
        }

        donation.setStatus(DonationStatus.CANCELLED);
        donation.setUpdatedAt(LocalDateTime.now());

        return donationRepository.save(donation);
    }

    @Transactional
    public Donation complete(Long donationId) {

        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));

        /*
         * Only a registered donation can be completed.
         */
        if (donation.getStatus() != DonationStatus.REGISTERED) {
            throw new InvalidStateException("Donation cannot be completed");
        }

        LocalDateTime now = LocalDateTime.now();

        donation.setStatus(DonationStatus.COMPLETED);
        donation.setDonatedAt(now);
        donation.setUpdatedAt(now);

        return donationRepository.save(donation);
    }

    @Transactional(readOnly = true)
    public boolean hasRegisteredDonation(Long userId) {

        return donationRepository.existsByUser_IdAndStatus(
                userId,
                DonationStatus.REGISTERED
        );
    }

    @Transactional(readOnly = true)
    public boolean isEligibleToDonate(Long userId) {

        Optional<Donation> lastCompletedDonation =
                donationRepository
                        .findFirstByUser_IdAndStatusAndDonatedAtIsNotNullOrderByDonatedAtDesc(
                                userId,
                                DonationStatus.COMPLETED
                        );

        if (lastCompletedDonation.isEmpty()) {
            return true;
        }

        Donation donation = lastCompletedDonation.get();

        LocalDateTime eligibleAt =
                donation.getDonatedAt().plusDays(recoveryDays);

        return !LocalDateTime.now().isBefore(eligibleAt);
    }
}