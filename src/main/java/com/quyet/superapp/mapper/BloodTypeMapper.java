package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodTypeDTO;
import com.quyet.superapp.entity.BloodType;

public class BloodTypeMapper {
    public static BloodTypeDTO toDTO(BloodType type) {
        return new BloodTypeDTO(
                type.getBloodTypeId(),
                type.getDescription()
        );
    }

    public static BloodType toEntity(BloodTypeDTO dto) {
        BloodType entity = new BloodType();
        entity.setBloodTypeId(dto.getBloodTypeId());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
