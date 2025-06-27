package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.SeparationPresetConfigDTO;
import com.quyet.superapp.entity.SeparationPresetConfig;

public class SeparationPresetConfigMapper {

    public static SeparationPresetConfigDTO toDTO(SeparationPresetConfig entity) {
        return new SeparationPresetConfigDTO(
                entity.getId(),
                entity.getGender(),
                entity.getMinWeight(),
                entity.getMethod(),
                entity.isLeukoreduced(),
                entity.getRbcRatio(),
                entity.getPlasmaRatio(),
                entity.getPlateletsFixed()
        );
    }

    public static SeparationPresetConfig fromDTO(SeparationPresetConfigDTO dto) {
        SeparationPresetConfig entity = new SeparationPresetConfig();
        entity.setId(dto.getId());
        entity.setGender(dto.getGender());
        entity.setMinWeight(dto.getMinWeight());
        entity.setMethod(dto.getMethod());
        entity.setLeukoreduced(dto.isLeukoreduced());
        entity.setRbcRatio(dto.getRbcRatio());
        entity.setPlasmaRatio(dto.getPlasmaRatio());
        entity.setPlateletsFixed(dto.getPlateletsFixed());
        return entity;
    }
}
