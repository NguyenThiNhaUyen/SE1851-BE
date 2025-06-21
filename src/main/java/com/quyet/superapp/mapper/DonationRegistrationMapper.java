package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.City;
import com.quyet.superapp.entity.address.District;
import com.quyet.superapp.entity.address.Ward;

public class DonationRegistrationMapper {

    //convert từ Entity -> DTO(để trà về front end)
    public static DonationRegistrationDTO toDTO(DonationRegistration entity) {
        UserProfile userProfile = entity.getUser().getUserProfile();
        DonationRegistrationDTO dto = new DonationRegistrationDTO();
        dto.setRegistrationId(entity.getRegistrationId());
        dto.setScheduledDate(entity.getReadyDate());
        dto.setLocation(entity.getLocation());
        dto.setBloodType(entity.getBloodType());
        dto.setStatus(entity.getStatus().name());

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (userProfile != null) {
            dto.setFullName(userProfile.getFullName());
            dto.setDob(userProfile.getDob());
            dto.setGender(userProfile.getGender());
            dto.setPhone(userProfile.getPhone());
            // ✅ Gán địa chỉ dạng chuỗi đầy đủ
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

    // Convert từ DTO → Entity (tạo đơn đăng ký mới)
    public static DonationRegistration toEntity(DonationRegistrationDTO dto, User user) {
        DonationRegistration entity = new DonationRegistration();
        entity.setUser(user);
        entity.setReadyDate(dto.getScheduledDate());
        entity.setLocation(dto.getLocation());
        entity.setBloodType(dto.getBloodType());
        return entity;
    }
}