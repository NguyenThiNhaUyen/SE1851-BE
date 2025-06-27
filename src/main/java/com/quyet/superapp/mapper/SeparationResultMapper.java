package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodSeparationSuggestionDTO;
import com.quyet.superapp.dto.BloodUnitDTO;
import com.quyet.superapp.dto.SeparationResultDTO;
import com.quyet.superapp.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class SeparationResultMapper {
    public static SeparationResultDTO toDTO(
            SeparationResult entity,
            BloodSeparationSuggestion suggestion,
            List<BloodUnit> createdUnits
    ) {
        BloodSeparationSuggestionDTO suggestionDTO = BloodSeparationSuggestionMapper.toDTO(suggestion);

        List<BloodUnitDTO> unitDTOs = createdUnits.stream()
                .map(BloodUnitMapper::toDTO)
                .collect(Collectors.toList());

        SeparationResultDTO dto = new SeparationResultDTO();
        dto.setSeparationOrderId(
                entity.getOrder() != null ? entity.getOrder().getSeparationOrderId() : null
        );
        dto.setSuggestion(suggestionDTO);
        dto.setCreatedUnits(unitDTOs);
        return dto;
    }
    public static SeparationResult fromDTO(
            SeparationResultDTO dto,
            SeparationOrder order,
            User processedBy
    ) {
        SeparationResult result = new SeparationResult();
        result.setOrder(order);
        result.setProcessedBy(processedBy);
        result.setCompletedAt(java.time.LocalDateTime.now());
        result.setNote(dto.getNote() ); // hoặc dto.getNote() nếu bạn có
        return result;
    }

}
