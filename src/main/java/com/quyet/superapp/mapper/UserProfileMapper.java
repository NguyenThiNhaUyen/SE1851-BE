package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.UserProfileDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;

public class UserProfileMapper {

    // Chuyển từ entity sang DTO
    public static UserProfileDTO toDTO(UserProfile profile) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(profile.getUser().getUserId());
        dto.setFullName(profile.getFullName());
        dto.setDob(profile.getDob());
        dto.setGender(profile.getGender());
        dto.setBloodType(profile.getBloodType());
        dto.setAddress(profile.getAddress());
        dto.setPhone(profile.getPhone());
        dto.setLandline(profile.getLandline());
        dto.setEmail(profile.getEmail());
        dto.setOccupation(profile.getOccupation());
        dto.setLastDonationDate(profile.getLastDonationDate());
        dto.setRecoveryTime(profile.getRecoveryTime());
        dto.setLocation(profile.getLocation());
        dto.setCitizenId(profile.getCitizenId());
        return dto;
    }

    // Chuyển từ DTO sang entity
    public static UserProfile toEntity(UserProfileDTO dto, User user) {
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setFullName(dto.getFullName());
        profile.setDob(dto.getDob());
        profile.setGender(dto.getGender());
        profile.setBloodType(dto.getBloodType());
        profile.setAddress(dto.getAddress());
        profile.setPhone(dto.getPhone());
        profile.setLandline(dto.getLandline());
        profile.setEmail(dto.getEmail());
        profile.setOccupation(dto.getOccupation());
        profile.setLastDonationDate(dto.getLastDonationDate());
        profile.setRecoveryTime(dto.getRecoveryTime());
        profile.setLocation(dto.getLocation());
        profile.setCitizenId(dto.getCitizenId());
        return profile;
    }
}
