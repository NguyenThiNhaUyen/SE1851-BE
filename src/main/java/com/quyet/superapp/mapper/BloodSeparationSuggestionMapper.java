package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodSeparationSuggestionDTO;
import com.quyet.superapp.entity.BloodSeparationSuggestion;
import com.quyet.superapp.entity.User;

import java.time.LocalDateTime;

public class BloodSeparationSuggestionMapper {
    public static BloodSeparationSuggestionDTO toDTO(BloodSeparationSuggestion entity) {
        if (entity == null || entity.getSuggestedConfig() == null) return null;

        String[] parts = entity.getSuggestedConfig().split(";");
        int rbc = 0, plasma = 0, platelet = 0;
        for (String p : parts) {
            if (p.startsWith("RBC=")) rbc = Integer.parseInt(p.substring(4));
            else if (p.startsWith("PLASMA=")) plasma = Integer.parseInt(p.substring(7));
            else if (p.startsWith("PLT=")) platelet = Integer.parseInt(p.substring(4));
        }

        BloodSeparationSuggestionDTO dto = new BloodSeparationSuggestionDTO();
        dto.setRedCellsMl(rbc);
        dto.setPlasmaMl(plasma);
        dto.setPlateletsMl(platelet);

        dto.setRedCellLabel("Hồng cầu: " + rbc + "ml");
        dto.setPlasmaLabel("Huyết tương: " + plasma + "ml");
        dto.setPlateletsLabel("Tiểu cầu: " + platelet + "ml");
        dto.setNote("Đề xuất tự động • Độ tin cậy: " + entity.getConfidenceScore());
        return dto;
    }

    /**
     * Tạo entity từ DTO + dữ liệu hệ thống
     */
    public static BloodSeparationSuggestion fromDTO(
            BloodSeparationSuggestionDTO dto,
            double confidenceScore,
            String inputSummary,
            User generatedBy
    ) {
        BloodSeparationSuggestion entity = new BloodSeparationSuggestion();
        entity.setCreatedAt(LocalDateTime.now());
        entity.setGeneratedBy(generatedBy);
        entity.setConfidenceScore(confidenceScore);
        entity.setInputDataSummary(inputSummary);

        // Tạo cấu hình đề xuất theo format: RBC=xxx;PLASMA=xxx;PLT=xxx
        String config = String.format("RBC=%d;PLASMA=%d;PLT=%d",
                dto.getRedCellsMl(),
                dto.getPlasmaMl(),
                dto.getPlateletsMl()
        );
        entity.setSuggestedConfig(config);

        return entity;
    }
}
