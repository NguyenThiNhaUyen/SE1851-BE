package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodSeparationDetailDTO;
import com.quyet.superapp.entity.BloodSeparationDetail;

public class BloodSeparationDetailMapper {


    public static BloodSeparationDetailDTO  toDTO(BloodSeparationDetail entity) {
        return new BloodSeparationDetailDTO(
                entity.getId(),
                entity.getSeparationLog().getBloodSeparationLogId(),
                entity.getBloodType().getBloodTypeId(),
                entity.getComponent().getBloodComponentId(),
                entity.getVolumeMl(),
                entity.getUnitCode(),
                entity.getCreatedAt()
        );
    }

    public static BloodSeparationDetail toEntity(BloodSeparationDetailDTO dto) {
        BloodSeparationDetail entity = new BloodSeparationDetail();
        entity.setId(dto.getId());
        entity.setVolumeMl(dto.getVolumeMl());
        entity.setUnitCode(dto.getUnitCode());
        return entity;
    }
}
