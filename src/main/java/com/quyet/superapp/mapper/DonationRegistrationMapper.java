package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.DonationRegistrationDTO;
<<<<<<< HEAD
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.City;
import com.quyet.superapp.entity.address.District;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.enums.DonationStatus;

public class DonationRegistrationMapper {

    //convert từ Entity -> DTO(để trà về front end)
    public static DonationRegistrationDTO toDTO(DonationRegistration entity) {
        User user = entity.getUser();
        //UserProfile userProfile = user.getUserProfile();
        UserProfile userProfile = entity.getUser().getUserProfile();
=======
import com.quyet.superapp.entity.*;
import com.quyet.superapp.entity.address.*;

public class DonationRegistrationMapper {

    // Convert từ Entity → DTO (trả về frontend)
    public static DonationRegistrationDTO toDTO(DonationRegistration entity) {
        User user = entity.getUser();
        UserProfile userProfile = entity.getUser().getUserProfile();

>>>>>>> origin/main
        DonationRegistrationDTO dto = new DonationRegistrationDTO();
        dto.setRegistrationId(entity.getRegistrationId());
        dto.setScheduledDate(entity.getReadyDate());
        dto.setLocation(entity.getLocation());
<<<<<<< HEAD
        dto.setBloodTypeDescription(entity.getBloodType());
        dto.setStatus(entity.getStatus().name());

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // ✅ Thêm userId và email
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());

        // ✅ Thêm thông tin người dùng nếu có profile
=======
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

>>>>>>> origin/main
        if (userProfile != null) {
            dto.setFullName(userProfile.getFullName());
            dto.setDob(userProfile.getDob());
            dto.setGender(userProfile.getGender());
            dto.setPhone(userProfile.getPhone());

<<<<<<< HEAD
            // ✅ Gán địa chỉ dạng chuỗi đầy đủ
=======
>>>>>>> origin/main
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

<<<<<<< HEAD
    // Convert từ DTO → Entity (tạo đơn đăng ký mới)
=======
    // Convert từ DTO → Entity (khi đăng ký mới)
>>>>>>> origin/main
    public static DonationRegistration toEntity(DonationRegistrationDTO dto, User user) {
        DonationRegistration entity = new DonationRegistration();
        entity.setUser(user);
        entity.setReadyDate(dto.getScheduledDate());
        entity.setLocation(dto.getLocation());
<<<<<<< HEAD
        entity.setStatus(DonationStatus.PENDING); // ✅ thêm dòng này
        return entity;
    }
}
=======
        entity.setBloodType(dto.getBloodType());
        entity.setEstimatedVolume(dto.getEstimatedVolume());
        entity.setNote(dto.getNote());

        // Slot sẽ được gán sau ở service bằng slotId
        return entity;
    }
}
>>>>>>> origin/main
