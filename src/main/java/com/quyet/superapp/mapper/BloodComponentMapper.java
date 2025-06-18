package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodComponentDTO;
import com.quyet.superapp.entity.BloodComponent;

public class BloodComponentMapper {
    public static BloodComponentDTO toDTO(BloodComponent entity) {
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
        BloodComponent component = new BloodComponent();
        component.setBloodComponentId(dto.getBloodComponentId());
        component.setName(dto.getName());
        component.setCode(dto.getCode());
        component.setStorageTemp(dto.getStorageTemp());
        component.setStorageDays(dto.getStorageDays());
        component.setUsage(dto.getUsage());
        component.setIsApheresisCompatible(dto.getIsApheresisCompatible());
        return component;
    }
}
