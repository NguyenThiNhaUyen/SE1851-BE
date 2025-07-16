package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.dto.DonationSlotDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.entity.address.*;

public class DonationRegistrationMapper {

    // Convert từ Entity → DTO (trả về frontend)
    public static DonationRegistrationDTO toDTO(DonationRegistration entity) {
        User user = entity.getUser();
        UserProfile userProfile = entity.getUser().getUserProfile();

        DonationRegistrationDTO dto = new DonationRegistrationDTO();
        dto.setRegistrationId(entity.getRegistrationId());
        dto.setScheduledDate(entity.getReadyDate());
        dto.setLocation(entity.getLocation());
        dto.setBloodType(entity.getBloodType());
        dto.setStatus(entity.getStatus().name());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());


        // Gán slotId và slotDTO nếu có
        if (entity.getSlot() != null) {
            dto.setSlotId(entity.getSlot().getSlotId());
            dto.setSlot(DonationSlotMapper.toDTO(entity.getSlot()));
        }

        if (userProfile != null) {
            dto.setFullName(userProfile.getFullName());
            dto.setDob(userProfile.getDob());
            dto.setGender(userProfile.getGender());
            dto.setPhone(userProfile.getPhone());

            if (userProfile.getAddress() != null) {
                Address addr = userProfile.getAddress();
                String full = addr.getAddressStreet();

                Ward ward = addr.getWard();
                if (ward != null) {
                    full += ", " + ward.getWardName();
                    District district = ward.getDistrict();
                    if (district != null) {
                        full += ", " + district.getDistrictName();
                        City city = district.getCity();
                        if (city != null) {
                            full += ", " + city.getNameCity();
                        }
                    }
                }

                dto.setAddressFull(full);
            }
        }

        return dto;
    }

    // Convert từ DTO → Entity (khi đăng ký mới)
    public static DonationRegistration toEntity(DonationRegistrationDTO dto, User user) {
        DonationRegistration entity = new DonationRegistration();
        entity.setUser(user);
        entity.setReadyDate(dto.getScheduledDate());
        entity.setLocation(dto.getLocation());
        entity.setBloodType(dto.getBloodType());
        entity.setEstimatedVolume(dto.getEstimatedVolume());
        entity.setNote(dto.getNote());

        // Slot sẽ được gán sau ở service bằng slotId
        return entity;
    }
}
