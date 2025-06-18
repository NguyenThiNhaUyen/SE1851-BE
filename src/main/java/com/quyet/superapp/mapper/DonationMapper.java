package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.DonationRequestDTO;
import com.quyet.superapp.entity.Donation;

public class DonationMapper {
    public static DonationRequestDTO toDTO(Donation donation) {
        DonationRequestDTO dto = new DonationRequestDTO();
        dto.setDonationId(donation.getDonationId());
        dto.setUserId(donation.getUser() != null ? donation.getUser().getUserId() : null);
        dto.setRegistrationId(donation.getRegistration() != null ? donation.getRegistration().getRegistrationId() : null);
        dto.setBloodTypeId(donation.getBloodType() != null ? donation.getBloodType().getBloodTypeId() : null);
        dto.setComponentId(donation.getComponent() != null ? donation.getComponent().getBloodComponentId() : null);
        dto.setDonationDate(donation.getDonationDate());
        dto.setVolumeMl(donation.getVolumeMl());
        dto.setLocation(donation.getLocation());
        dto.setNotes(donation.getNotes());
        // Nếu cần:
        dto.setWeight(donation.getUser().getUserProfile().getWeight());

        return dto;
    }
}