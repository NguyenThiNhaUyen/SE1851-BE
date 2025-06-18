package com.quyet.superapp.util;

import com.quyet.superapp.dto.BloodSeparationSuggestionDTO;
import org.springframework.stereotype.Component;

@Component
public class BloodSeparationCalculator {

    public BloodSeparationSuggestionDTO calculate(
            int volume,
            double redRatio,
            double plasmaRatio,
            int plateletsFixed,
            String method,
            String gender,
            Double weight,
            String bloodGroup
    ) {
        int red = (int) (volume * redRatio);
        int plasma = (int) (volume * plasmaRatio);
        int platelets = volume - red - plasma;

        if (platelets < plateletsFixed) {
            platelets = plateletsFixed;
            plasma = volume - red - platelets;
        }

        return new BloodSeparationSuggestionDTO(
                red,
                plasma,
                platelets,
                "PRC-" + bloodGroup,
                "FFP-" + bloodGroup,
                "PLT-" + bloodGroup,
                String.format("Theo preset: %s - %s - %dkg", method, gender, weight)
        );
    }
}
