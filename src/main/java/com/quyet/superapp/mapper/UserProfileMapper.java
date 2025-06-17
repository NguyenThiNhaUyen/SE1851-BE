package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.UserProfileDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.entity.address.District;
import com.quyet.superapp.entity.address.City;

import java.time.format.DateTimeFormatter;

public class UserProfileMapper {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static UserProfileDTO toDTO(UserProfile profile) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(profile.getUser() != null ? profile.getUser().getUserId() : null);
        dto.setFullName(profile.getFullName());

        if (profile.getDob() != null) {
            dto.setDob(profile.getDob().format(DATE_FORMAT));
        }

        dto.setGender(profile.getGender());
        dto.setBloodType(profile.getBloodType());
        dto.setPhone(profile.getPhone());

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

    public static UserProfile toEntity(UserProfileDTO dto, User user, Address address) {
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setFullName(dto.getFullName());

        if (dto.getDob() != null && !dto.getDob().isEmpty()) {
            profile.setDob(java.time.LocalDate.parse(dto.getDob(), DATE_FORMAT));
        }

        profile.setGender(dto.getGender());
        profile.setBloodType(dto.getBloodType());
        profile.setPhone(dto.getPhone());
        profile.setAddress(address);

        return profile;
    }
}
