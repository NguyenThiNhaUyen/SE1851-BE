package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.DonationRequestDTO;
import com.quyet.superapp.entity.Donation;

public class DonationRequestMapper {
    public static DonationRequestDTO toDTO(Donation donation) {
        DonationRequestDTO dto = new DonationRequestDTO();
        dto.setDonationId(donation.getDonationId());

        if (donation.getUser() != null) {
            dto.setUserId(donation.getUser().getUserId());
            if (donation.getUser().getUserProfile() != null) {
                dto.setWeight(donation.getUser().getUserProfile().getWeightKg());
            }
        }

        if (donation.getRegistration() != null)
            dto.setRegistrationId(donation.getRegistration().getRegistrationId());

        if (donation.getBloodType() != null)
            dto.setBloodTypeId(donation.getBloodType().getBloodTypeId());

        if (donation.getComponent() != null)
            dto.setComponentId(donation.getComponent().getBloodComponentId());

        dto.setDonationDate(donation.getCollectedAt());
        dto.setVolumeMl(donation.getVolumeMl());
        dto.setLocation(donation.getLocation());
        dto.setNotes(donation.getNotes());
        dto.setCreatedAt(donation.getCreatedAt());
        dto.setUpdatedAt(donation.getUpdatedAt());
        dto.setStatus(donation.getStatus());
        return dto;
    }
}