package com.donar.api.donation.dto;

public record DonationStatisticsResponse(
        long registeredDonations,
        long completedDonations,
        long cancelledDonations
) {
}
