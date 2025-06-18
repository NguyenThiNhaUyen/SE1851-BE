package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.UserProfileDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.entity.address.District;
import com.quyet.superapp.entity.address.City;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserProfileMapper {


    // Chuyển từ entity sang DTO

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ✅ Entity → DTO (chuyển đúng kiểu và có địa chỉ đầy đủ)
    public static UserProfileDTO toDTO(UserProfile profile) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(profile.getUser() != null ? profile.getUser().getUserId() : null);
        dto.setFullName(profile.getFullName());
        if (profile.getDob() != null) {
            dto.setDob(profile.getDob());
        }
        dto.setGender(profile.getGender());
        dto.setBloodType(profile.getBloodType());
        dto.setPhone(profile.getPhone());
        dto.setLandline(profile.getLandline());
        dto.setEmail(profile.getEmail());
        dto.setOccupation(profile.getOccupation());
        dto.setLastDonationDate(profile.getLastDonationDate());
        dto.setRecoveryTime(profile.getRecoveryTime());
        dto.setLocation(profile.getLocation());
        dto.setCitizenId(profile.getCitizenId());

        // ✅ Build full address
        if (profile.getAddress() != null) {
            Address addr = profile.getAddress();
            Ward ward = addr.getWard();
            District district = ward.getDistrict();
            City city = district.getCity();

            String fullAddress = String.format(
                    "%s, %s, %s, %s",
                    addr.getAddressStreet(),
                    ward.getWardName(),
                    district.getDistrictName(),
                    city.getNameCity()
            );
            dto.setAddressFull(fullAddress);
        }

        return dto;
    }
    // Chuyển từ DTO sang entity
    public static UserProfile toEntity(UserProfileDTO dto, User user) {
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setFullName(dto.getFullName());
        profile.setDob(dto.getDob());

        if (profile.getAddress() != null) {
            Address addr = profile.getAddress();
            Ward ward = addr.getWard();
            District district = ward.getDistrict();
            City city = district.getCity();

            String fullAddress = String.format(
                    "%s, %s, %s, %s",
                    addr.getAddressStreet(),
                    ward.getWardName(),
                    district.getDistrictName(),
                    city.getNameCity()
            );
            dto.setAddressFull(fullAddress);
        }

        return profile;
    }

    // ✅ DTO → Entity (không còn lỗi NullPointerException)
    public static UserProfile toEntity(UserProfileDTO dto, User user, Address address) {
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        if (dto.getDob() != null) {
            profile.setDob(dto.getDob()); // String → LocalDate
        }
        profile.setFullName(dto.getFullName());
        profile.setGender(dto.getGender());
        profile.setBloodType(dto.getBloodType());
        profile.setPhone(dto.getPhone());
        profile.setLandline(dto.getLandline());
        profile.setEmail(dto.getEmail());
        profile.setOccupation(dto.getOccupation());
        profile.setLastDonationDate(dto.getLastDonationDate());
        profile.setRecoveryTime(dto.getRecoveryTime());
        profile.setLocation(dto.getLocation());
        profile.setCitizenId(dto.getCitizenId());
        profile.setAddress(address);
        return profile;
    }
}
