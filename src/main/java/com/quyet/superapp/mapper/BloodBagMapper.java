package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodBagDTO;
import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.entity.BloodType;
<<<<<<< HEAD

public class BloodBagMapper {
    public static BloodBagDTO toDTO(BloodBag entity) {
        return new BloodBagDTO(
                entity.getBloodBagId(),
                entity.getBagCode(),
                entity.getBloodType() != null ? String.valueOf(entity.getBloodType().getBloodTypeId()) : null,
                entity.getRh(),
                entity.getVolume(),
                entity.getHematocrit(),
                entity.getCollectedAt(),
                entity.getTestStatus(),
                entity.getStatus(),
                entity.getDonorId(),
                entity.getNote()
        );
    }

    public static BloodBag fromDTO(BloodBagDTO dto, BloodType bloodType) {
=======
import com.quyet.superapp.entity.User;

public class BloodBagMapper {

    public static BloodBagDTO toDTO(BloodBag entity) {
        BloodBagDTO dto = new BloodBagDTO();
        dto.setBloodBagId(entity.getBloodBagId());
        dto.setBagCode(entity.getBagCode());

        // ✅ Tách BloodType entity
        if (entity.getBloodType() != null) {
            dto.setBloodTypeId(entity.getBloodType().getBloodTypeId());
            dto.setBloodTypeName(entity.getBloodType().getDescription());
        }

        dto.setRh(entity.getRh());
        dto.setVolume(entity.getVolume());
        dto.setHematocrit(entity.getHematocrit());
        dto.setCollectedAt(entity.getCollectedAt());
        dto.setTestStatus(entity.getTestStatus());
        dto.setStatus(entity.getStatus());

        // ✅ Chỉ lấy thông tin cơ bản từ User
        if (entity.getDonor() != null && entity.getDonor().getUserProfile() != null) {
            dto.setDonorId(entity.getDonor().getUserId());
            dto.setDonorName(entity.getDonor().getUserProfile().getFullName());
            dto.setDonorPhone(entity.getDonor().getUserProfile().getPhone());
        }

        dto.setNote(entity.getNote());
        return dto;
    }

    public static BloodBag fromDTO(BloodBagDTO dto, BloodType bloodType, User donor) {
>>>>>>> origin/main
        BloodBag entity = new BloodBag();
        entity.setBloodBagId(dto.getBloodBagId());
        entity.setBagCode(dto.getBagCode());
        entity.setBloodType(bloodType);
        entity.setRh(dto.getRh());
        entity.setVolume(dto.getVolume());
        entity.setHematocrit(dto.getHematocrit());
        entity.setCollectedAt(dto.getCollectedAt());
        entity.setTestStatus(dto.getTestStatus());
        entity.setStatus(dto.getStatus());
<<<<<<< HEAD
        entity.setDonorId(dto.getDonorId());
=======
        entity.setDonor(donor); // ✅ donor entity phải được fetch ở service
>>>>>>> origin/main
        entity.setNote(dto.getNote());
        return entity;
    }
}
<<<<<<< HEAD
=======


>>>>>>> origin/main
