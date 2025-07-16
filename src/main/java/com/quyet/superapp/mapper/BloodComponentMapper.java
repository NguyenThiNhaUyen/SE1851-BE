package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodComponentDTO;
import com.quyet.superapp.entity.BloodComponent;

public class BloodComponentMapper {

    public static BloodComponentDTO toDTO(BloodComponent entity) {
        if (entity == null) return null;

        return new BloodComponentDTO(
                entity.getBloodComponentId(),
                entity.getName(),
                entity.getCode(),
                entity.getStorageTemp(),
                entity.getStorageDays(),
                entity.getUsage(),
                entity.getIsApheresisCompatible()
        );
    }

    public static BloodComponent toEntity(BloodComponentDTO dto) {
        if (dto == null) return null;

        BloodComponent entity = new BloodComponent();
        entity.setBloodComponentId(dto.getBloodComponentId());
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setStorageTemp(dto.getStorageTemp());
        entity.setStorageDays(dto.getStorageDays());
        entity.setUsage(dto.getUsage());
        entity.setIsApheresisCompatible(dto.getIsApheresisCompatible());
        return entity;
    }
}
