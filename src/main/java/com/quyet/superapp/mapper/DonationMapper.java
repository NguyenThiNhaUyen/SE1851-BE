package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.DonationDTO;
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.User;

public class DonationMapper {

    public static DonationDTO toDTO(Donation donation) {
        if (donation == null) return null;

        DonationDTO dto = new DonationDTO();
        dto.setDonationId(donation.getDonationId());
        dto.setRegistrationId(donation.getRegistration().getRegistrationId());
        dto.setVolume(donation.getVolumeMl());
        dto.setCollectedAt(donation.getCollectedAt());
        dto.setNote(donation.getNotes());
        dto.setStatus(donation.getStatus().name());

        if (donation.getHandledBy() != null)
            dto.setStaffId(donation.getHandledBy().getUserId());

        if (donation.getBloodBag() != null)
            dto.setBloodBagCode(donation.getBloodBag().getBagCode());

        // Optional: user + registration if cần UI
        return dto;
    }

    public static Donation toEntity(DonationDTO dto, User staff, DonationRegistration reg) {
        Donation donation = new Donation();
        donation.setRegistration(reg);
        donation.setHandledBy(staff);
        donation.setVolumeMl(dto.getVolume());
        donation.setCollectedAt(dto.getCollectedAt());
        donation.setNotes(dto.getNote());
        // status có thể set ở service (ex: COMPLETED)
        return donation;
    }
}
