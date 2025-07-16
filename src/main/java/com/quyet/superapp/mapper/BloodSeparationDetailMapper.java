package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodSeparationDetailDTO;
import com.quyet.superapp.dto.BloodSeparationDetailFullDTO;
import com.quyet.superapp.entity.BloodSeparationDetail;
import com.quyet.superapp.entity.SeparationResult;
import com.quyet.superapp.enums.BloodComponentType;

import java.time.LocalDateTime;

public class BloodSeparationDetailMapper {

    public static BloodSeparationDetailDTO toDTO(BloodSeparationDetail entity) {
        BloodSeparationDetailDTO dto = new BloodSeparationDetailDTO();
        dto.setBloodSeparationDetailId(entity.getBloodSeparationDetailId());
        dto.setComponentType(entity.getComponentType());
        dto.setVolumeMl(entity.getVolumeMl());
        dto.setQualityRating(entity.getQualityRating());
        dto.setNote(entity.getNote());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static BloodSeparationDetail toEntity(BloodSeparationDetailDTO dto, SeparationResult result) {
        BloodSeparationDetail entity = new BloodSeparationDetail();
        entity.setBloodSeparationDetailId(dto.getBloodSeparationDetailId());
        entity.setResult(result);
        entity.setComponentType(dto.getComponentType());
        entity.setVolumeMl(dto.getVolumeMl());
        entity.setQualityRating(dto.getQualityRating());
        entity.setNote(dto.getNote());
        entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        return entity;
    }

    public static BloodSeparationDetailFullDTO toFullDTO(BloodSeparationDetail entity) {
        BloodSeparationDetailFullDTO dto = new BloodSeparationDetailFullDTO();
        dto.setBloodSeparationDetailId(entity.getBloodSeparationDetailId());
        dto.setComponentType(entity.getComponentType());
        dto.setVolumeMl(entity.getVolumeMl());
        dto.setQualityRating(entity.getQualityRating());
        dto.setNote(entity.getNote());
        dto.setCreatedAt(entity.getCreatedAt());

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

