
package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodSeparationSuggestionDTO;
import com.quyet.superapp.entity.BloodSeparationSuggestion;
import com.quyet.superapp.entity.User;

import java.time.LocalDateTime;

public class BloodSeparationSuggestionMapper {
    public static BloodSeparationSuggestionDTO toDTO(BloodSeparationSuggestion entity) {
        BloodSeparationSuggestionDTO dto = new BloodSeparationSuggestionDTO();
        dto.setRedCellsMl(entity.getRedCells());
        dto.setPlasmaMl(entity.getPlasma());
        dto.setPlateletsMl(entity.getPlatelets());
        dto.setRedCellLabel(entity.getRedCellsCode());
        dto.setPlasmaLabel(entity.getPlasmaCode());
        dto.setPlateletsLabel(entity.getPlateletsCode());
        dto.setNote(entity.getDescription());
        return dto;
    }


    public static BloodSeparationSuggestion fromDTO(BloodSeparationSuggestionDTO dto, User generatedBy) {
        BloodSeparationSuggestion entity = new BloodSeparationSuggestion();
        entity.setRedCells(dto.getRedCellsMl());
        entity.setPlasma(dto.getPlasmaMl());
        entity.setPlatelets(dto.getPlateletsMl());
        entity.setRedCellsCode(dto.getRedCellLabel());
        entity.setPlasmaCode(dto.getPlasmaLabel());
        entity.setPlateletsCode(dto.getPlateletsLabel());
        entity.setDescription(dto.getNote());
        entity.setGeneratedBy(generatedBy);
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }


}

