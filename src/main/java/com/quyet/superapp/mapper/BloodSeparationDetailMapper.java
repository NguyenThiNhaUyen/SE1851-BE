package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodSeparationDetailDTO;
import com.quyet.superapp.dto.BloodSeparationDetailFullDTO;
import com.quyet.superapp.entity.BloodSeparationDetail;
import com.quyet.superapp.entity.SeparationResult;
import com.quyet.superapp.enums.BloodComponentType;

public class BloodSeparationDetailMapper {
    public static BloodSeparationDetailDTO toDTO(BloodSeparationDetail entity) {
        BloodSeparationDetailDTO dto = new BloodSeparationDetailDTO();
        dto.setBloodSeparationDetailId(entity.getBloodSeparationDetailId());
        dto.setComponentType(entity.getComponentType());
        dto.setVolumeMl(entity.getVolumeMl());
        dto.setQualityRating(entity.getQualityRating());
        return dto;
    }

    public static BloodSeparationDetail toEntity(
            BloodSeparationDetailDTO dto,
            SeparationResult result
    ) {
        BloodSeparationDetail entity = new BloodSeparationDetail();
        entity.setBloodSeparationDetailId(dto.getBloodSeparationDetailId());
        entity.setResult(result);
        entity.setComponentType(dto.getComponentType());
        entity.setVolumeMl(dto.getVolumeMl());
        entity.setQualityRating(dto.getQualityRating());
        return entity;
    }

    public static BloodSeparationDetailFullDTO toFullDTO(BloodSeparationDetail entity) {
        BloodSeparationDetailFullDTO dto = new BloodSeparationDetailFullDTO();
        dto.setBloodSeparationDetailId(entity.getBloodSeparationDetailId());
        dto.setComponentType(entity.getComponentType());
        dto.setVolumeMl(entity.getVolumeMl());
        dto.setQualityRating(entity.getQualityRating());

        if (entity.getResult() != null) {
            dto.setSeparationResultId(entity.getResult().getSeparationResultId());
            if (entity.getResult().getOrder() != null && entity.getResult().getOrder().getBloodBag() != null) {
                dto.setBloodBagCode(entity.getResult().getOrder().getBloodBag().getBagCode());
            }
            if (entity.getResult().getProcessedBy() != null) {
                dto.setProcessedBy(entity.getResult().getProcessedBy().getUsername());
            }
        }
        return dto;
    }

}
