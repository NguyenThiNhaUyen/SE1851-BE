package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.UserProfileCreateDTO;
import com.quyet.superapp.dto.UserProfileUpdateDTO;
import com.quyet.superapp.dto.UserProfileResponseDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.City;
import com.quyet.superapp.entity.address.District;
import com.quyet.superapp.entity.address.Ward;

import java.time.LocalDate;

public class UserProfileMapper {

    // ✅ Entity → ResponseDTO
    public static UserProfileResponseDTO toResponseDTO(UserProfile profile) {
        if (profile == null) return null;

        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setUserId(profile.getUser() != null ? profile.getUser().getUserId() : null);
        dto.setFullName(profile.getFullName());
        dto.setDob(profile.getDob());
        dto.setGender(profile.getGender());
        dto.setBloodType(profile.getBloodType());
        dto.setPhone(profile.getPhone());
        dto.setEmail(profile.getEmail());
        dto.setOccupation(profile.getOccupation());
        dto.setLastDonationDate(LocalDate.from(profile.getLastDonationDate()));
        dto.setRecoveryTime(profile.getRecoveryTime());
        dto.setLocation(profile.getLocation());
        dto.setCitizenId(profile.getCitizenId());
        dto.setWeight(profile.getWeight());
        dto.setHeight(profile.getHeight());
        dto.setLatitude(profile.getLatitude());
        dto.setLongitude(profile.getLongitude());

        if (profile.getAddress() != null) {
            Address address = profile.getAddress();
            Ward ward = address.getWard();
            District district = ward.getDistrict();
            City city = district.getCity();

            String fullAddress = String.format(
                    "%s, %s, %s, %s",
                    address.getAddressStreet(),
                    ward.getWardName(),
                    district.getDistrictName(),
                    city.getNameCity()
            );

            dto.setAddressFull(fullAddress);
            dto.setAddressId(address.getAddressId());
        }

        return dto;
    }

    // ✅ CreateDTO → Entity: chỉ gán thông tin cơ bản
    public static UserProfile fromCreateDTO(UserProfileCreateDTO dto, User user, Address address) {
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setFullName(dto.getFullName());
        profile.setDob(dto.getDob());
        profile.setGender(dto.getGender());
        profile.setPhone(dto.getContactInfo().getPhone());
        profile.setEmail(dto.getContactInfo().getEmail());
        profile.setOccupation(dto.getOccupation());
        profile.setCitizenId(dto.getCitizenId());
        profile.setAddress(address);
        return profile;
    }

    // ✅ UpdateDTO → Entity: chỉ gán nếu dữ liệu không null
    public static void updateEntityFromDTO(UserProfile profile, UserProfileUpdateDTO dto, Address address) {
        if (dto.getFullName() != null) profile.setFullName(dto.getFullName());
        if (dto.getDob() != null) profile.setDob(dto.getDob());
        if (dto.getGender() != null) profile.setGender(dto.getGender());
        if (dto.getBloodType() != null) profile.setBloodType(dto.getBloodType());
        if (dto.getPhone() != null) profile.setPhone(dto.getPhone());
        if (dto.getEmail() != null) profile.setEmail(dto.getEmail());
        if (dto.getOccupation() != null) profile.setOccupation(dto.getOccupation());
        if (dto.getLastDonationDate() != null) profile.setLastDonationDate(dto.getLastDonationDate().atStartOfDay());
        if (dto.getRecoveryTime() != null) profile.setRecoveryTime(dto.getRecoveryTime());
        if (dto.getLocation() != null) profile.setLocation(dto.getLocation());
        if (dto.getCitizenId() != null) profile.setCitizenId(dto.getCitizenId());
        if (dto.getWeight() != null) profile.setWeight(dto.getWeight());
        if (dto.getHeight() != null) profile.setHeight(dto.getHeight());
        if (dto.getLatitude() != null) profile.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) profile.setLongitude(dto.getLongitude());

        if (address != null) {
            profile.setAddress(address);
        }
    }
}
