package com.quyet.superapp.util;

import com.quyet.superapp.dto.BloodSeparationSuggestionDTO;
import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.entity.BloodSeparationSuggestion;
import com.quyet.superapp.entity.SeparationPresetConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BloodSeparationCalculator {

    public BloodSeparationSuggestion calculateFromPreset(BloodBag bloodBag, SeparationPresetConfig preset) {
        int volume = bloodBag.getVolume();
        if (volume <= 0) {
            throw new IllegalArgumentException("Thể tích túi máu không hợp lệ.");
        }
        if (volume < 250) {
            log.warn("⚠️ Thể tích túi máu ({}ml) nhỏ hơn 250ml – có thể không phù hợp để tách.", volume);
        }


        String bloodGroup = (bloodBag.getBloodType() != null && bloodBag.getBloodType().getDescription() != null)
                ? bloodBag.getBloodType().getDescription()
                : "UNK";

        // 1. Tính thể tích theo preset
        int red = (int) Math.round(volume * preset.getRbcRatio());
        int plasma = (int) Math.round(volume * preset.getPlasmaRatio());
        int platelets = preset.getPlateletsFixed() > 0 ? preset.getPlateletsFixed() : volume - red - plasma;


        // 2. Ràng buộc: tổng không được vượt volume
        int total = red + plasma + platelets;
        if (total > volume) {
            int excess = total - volume;
            // Giảm đều từ plasma và platelets
            int half = excess / 2;
            plasma = Math.max(0, plasma - half);
            platelets = Math.max(0, platelets - (excess - half));

            log.info("🎯 Đã điều chỉnh plasma xuống {}ml và platelets xuống {}ml do vượt thể tích tổng.", plasma, platelets);
        }
        // 3. Ràng buộc y tế: không phần nào < 10ml
        if (red < 10 || plasma < 10 || platelets < 10) {
            log.error("❌ Thành phần máu nhỏ hơn 10ml (RBC={}, Plasma={}, PLT={}) – không đạt chuẩn.", red, plasma, platelets);
            throw new IllegalArgumentException("Một trong các thành phần máu có thể tích nhỏ hơn 10ml – không đạt tiêu chuẩn tách.");
        }

        // 4. Giới hạn tỷ lệ tối đa (ví dụ 70%)
        double redPercent = (double) red / volume;
        double plasmaPercent = (double) plasma / volume;

        if (redPercent > 0.7 || plasmaPercent > 0.7) {
            throw new IllegalArgumentException("Tỉ lệ RBC hoặc Plasma vượt quá giới hạn 70%.");
        }

        // 5. Tạo đối tượng gợi ý entity
        BloodSeparationSuggestion suggestion = new BloodSeparationSuggestion();
        suggestion.setRedCells(red);
        suggestion.setPlasma(plasma);
        suggestion.setPlatelets(platelets);
        suggestion.setRedCellsCode("PRC-" + bloodGroup);
        suggestion.setPlasmaCode("FFP-" + bloodGroup);
        suggestion.setPlateletsCode("PLT-" + bloodGroup);
        suggestion.setDescription(String.format(
                "Preset: %s - %s - %skg",
                preset.getMethod(),
                preset.getGender(),
                preset.getMinWeight()
        ));

        return suggestion;
    }
}
