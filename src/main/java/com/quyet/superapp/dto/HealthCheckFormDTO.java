package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class HealthCheckFormDTO {
    private Long id;
    private Long registrationId;

    private Double bodyTemperature;
    private Integer heartRate;
    private Integer bloodPressureSys;
    private Integer bloodPressureDia;
    private Double weightKg;

    private Boolean hasFever;
    private Boolean tookAntibioticsRecently;
    private Boolean hasChronicIllness;
    private Boolean isPregnantOrBreastfeeding;
    private Boolean hadRecentTattooOrSurgery;
    private Boolean hasRiskySexualBehavior;

    private Boolean isEligible;
    private String notesByStaff;

}
