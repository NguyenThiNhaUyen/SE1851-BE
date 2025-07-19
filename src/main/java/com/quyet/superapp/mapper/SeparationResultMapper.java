package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SeparationResultMapper {

    public static SeparationResultDTO toDTO(
            SeparationResult result,
            BloodSeparationSuggestionDTO suggestionDTO,
            List<BloodUnit> createdUnits
    ) {
        if (result == null) return null;

        SeparationResultDTO dto = new SeparationResultDTO();
        dto.setSeparationOrderId(
                result.getOrder() != null ? result.getOrder().getSeparationOrderId() : null
        );
        dto.setSuggestion(suggestionDTO);

        if (createdUnits != null) {
            dto.setCreatedUnits(createdUnits.stream()
                    .map(BloodUnitMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        if (result.getProcessedBy() != null) {
            dto.setProcessedById(result.getProcessedBy().getUserId());
            dto.setProcessedByName(
                    result.getProcessedBy().getUserProfile() != null
                            ? result.getProcessedBy().getUserProfile().getFullName()
                            : result.getProcessedBy().getUsername()
            );
        }

        dto.setNote(result.getNote());
        dto.setCompletedAt(result.getCompletedAt());

        return dto;
    }

    public static SeparationResult fromDTO(
            SeparationResultDTO dto,
            SeparationOrder order,
            BloodSeparationSuggestion suggestion,
            User processedBy
    ) {
        SeparationResult result = new SeparationResult();
        result.setOrder(order);
        result.setSuggestion(suggestion);
        result.setProcessedBy(processedBy);
        result.setNote(dto.getNote());
        result.setCompletedAt(
                dto.getCompletedAt() != null ? dto.getCompletedAt() : LocalDateTime.now()
        );
        return result;
    }
}
