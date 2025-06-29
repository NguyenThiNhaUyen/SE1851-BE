package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodSeparationResultDTO;
import com.quyet.superapp.dto.BloodUnitDTO;
import com.quyet.superapp.dto.BloodSeparationSuggestionDTO;
import com.quyet.superapp.dto.SeparationResultDTO;
import com.quyet.superapp.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class SeparationResultMapper {

    public static SeparationResultDTO toDTO(SeparationResult result, BloodSeparationSuggestionDTO suggestionDTO, List<BloodUnit> units) {
        SeparationResultDTO dto = new SeparationResultDTO();
        dto.setSeparationOrderId(result.getOrder().getSeparationOrderId());
        dto.setCreatedUnits(units.stream().map(BloodUnitMapper::toDTO).toList());
        dto.setNote(result.getNote());
        dto.setCompletedAt(result.getCompletedAt());

        // Gán suggestion đã có từ entity → chuyển thành DTO
        dto.setSuggestion(BloodSeparationSuggestionMapper.toDTO(result.getSuggestion()));

        if (result.getProcessedBy() != null) {
            dto.setProcessedById(result.getProcessedBy().getUserId());
            dto.setProcessedByName(result.getProcessedBy().getUsername());
        }
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
        result.setCompletedAt(dto.getCompletedAt() != null ? dto.getCompletedAt() : java.time.LocalDateTime.now());
        result.setNote(dto.getNote());
        return result;
    }

    public static BloodSeparationResultDTO toDetailDTO(
            SeparationResult result,
            List<BloodSeparationDetail> details
    ) {
        BloodSeparationResultDTO dto = new BloodSeparationResultDTO();
        dto.setSeparationId(result.getSeparationResultId());
        dto.setPerformedBy(result.getProcessedBy() != null ? result.getProcessedBy().getUsername() : null);
        dto.setSeparatedAt(result.getCompletedAt());

        dto.setComponents(details.stream()
                .map(BloodSeparationDetailMapper::toDTO)
                .collect(Collectors.toList()));

        return dto;
    }
}
