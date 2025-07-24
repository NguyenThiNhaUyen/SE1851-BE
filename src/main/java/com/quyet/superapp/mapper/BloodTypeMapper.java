package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodTypeDTO;
import com.quyet.superapp.entity.BloodType;

public class BloodTypeMapper {

    public static BloodTypeDTO toDTO(BloodType entity) {
        if (entity == null) return null;

        return new BloodTypeDTO(
                entity.getBloodTypeId(),
                entity.getDescription() // ví dụ: "A+"
        );
    }

    public static BloodType toEntity(BloodTypeDTO dto) {
        if (dto == null || dto.getDescription() == null) return null;

        BloodType entity = new BloodType();
        entity.setBloodTypeId(dto.getBloodTypeId());
        entity.setDescription(dto.getDescription().trim().toUpperCase()); // đảm bảo viết chuẩn hóa

        return entity;
    }
}
