package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.EmergencyDonorDTO;
import com.quyet.superapp.dto.UnverifiedDonorDTO;
import com.quyet.superapp.dto.VerifiedUrgentDonorDTO;
import com.quyet.superapp.entity.UrgentDonorRegistry;
import org.springframework.stereotype.Component;

@Component
public class UrgentDonorMapper {

    public VerifiedUrgentDonorDTO toVerifiedDTO(UrgentDonorRegistry entity) {
        var dto = new VerifiedUrgentDonorDTO();
        var user = entity.getDonor();

        dto.setUserId(user.getUserId());

        if (user.getUserProfile() != null) {
            var profile = user.getUserProfile();
            dto.setFullName(profile.getFullName());
            dto.setPhone(profile.getPhone());
            dto.setBloodType(profile.getBloodType() != null
                    ? profile.getBloodType().getDescription()
                    : "Chưa rõ");
            dto.setAddressFull(profile.getAddress() != null
                    ? profile.getAddress().getFullAddress()
                    : "Chưa cập nhật");
        } else {
            dto.setFullName("Chưa có hồ sơ");
            dto.setPhone("N/A");
            dto.setBloodType("N/A");
            dto.setAddressFull("N/A");
        }
        return dto;
    }


    public UnverifiedDonorDTO toUnverifiedDTO(UrgentDonorRegistry entity) {
            var dto = new UnverifiedDonorDTO();
            dto.setDonorRegistryId(entity.getId()); // ✅ Thêm dòng này
            var user = entity.getDonor();
            dto.setUserId(user.getUserId());

            if (user.getUserProfile() != null) {
                var profile = user.getUserProfile();
                dto.setFullName(profile.getFullName());
                dto.setPhone(profile.getPhone());
                dto.setGender(profile.getGender());
                dto.setDob(profile.getDob());
                dto.setBloodType(profile.getBloodType() != null ? profile.getBloodType().getDescription() : "Chưa rõ");
                dto.setAddressFull(profile.getAddress() != null ? profile.getAddress().getFullAddress() : "N/A");
            } else {
                dto.setFullName("Chưa có hồ sơ");
                dto.setPhone("N/A");
                dto.setGender("N/A");
                dto.setDob(null);
                dto.setAddressFull("N/A");
                dto.setBloodType("N/A");
            }

            dto.setRegisteredAt(entity.getRegisteredAt());
            dto.setStatus(Boolean.TRUE.equals(entity.getIsVerified()) ? "ĐÃ XÁC MINH" : "CHỜ XÁC MINH");
            return dto;
        }
    }

