package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.HealthCheckFormDTO;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.HealthCheckForm;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckFormMapper {

    public HealthCheckForm toEntity(HealthCheckFormDTO dto, DonationRegistration reg, boolean isEligible) {
        return HealthCheckForm.builder()
                .registration(reg)
                .bodyTemperature(dto.getBodyTemperature())
                .heartRate(dto.getHeartRate())
                .bloodPressureSys(dto.getBloodPressureSys())
                .bloodPressureDia(dto.getBloodPressureDia())
                .weightKg(dto.getWeightKg())
                .heightCm(dto.getHeightCm())
                .hasFever(dto.getHasFever())
                .tookAntibioticsRecently(dto.getTookAntibioticsRecently())
                .hasChronicIllness(dto.getHasChronicIllness())
                .isPregnantOrBreastfeeding(dto.getIsPregnantOrBreastfeeding())
                .hadRecentTattooOrSurgery(dto.getHadRecentTattooOrSurgery())
                .hasRiskySexualBehavior(dto.getHasRiskySexualBehavior())
                .isEligible(isEligible)
                .notesByStaff(dto.getNotesByStaff())

                // ✅ Gộp thêm thông số xét nghiệm máu
                .hemoglobin(dto.getHemoglobin())
                .hbsAgPositive(dto.getHbsAgPositive())
                .hcvPositive(dto.getHcvPositive())
                .hivPositive(dto.getHivPositive())
                .syphilisPositive(dto.getSyphilisPositive())
                .build();
    }

    public HealthCheckFormDTO toDTO(HealthCheckForm entity) {
        HealthCheckFormDTO dto = new HealthCheckFormDTO();
        dto.setId(entity.getId());
        dto.setRegistrationId(entity.getRegistration().getRegistrationId());
        dto.setBodyTemperature(entity.getBodyTemperature());
        dto.setHeartRate(entity.getHeartRate());
        dto.setBloodPressureSys(entity.getBloodPressureSys());
        dto.setBloodPressureDia(entity.getBloodPressureDia());
        dto.setWeightKg(entity.getWeightKg());
        dto.setHeightCm(entity.getHeightCm());
        dto.setHasFever(entity.getHasFever());
        dto.setTookAntibioticsRecently(entity.getTookAntibioticsRecently());
        dto.setHasChronicIllness(entity.getHasChronicIllness());
        dto.setIsPregnantOrBreastfeeding(entity.getIsPregnantOrBreastfeeding());
        dto.setHadRecentTattooOrSurgery(entity.getHadRecentTattooOrSurgery());
        dto.setHasRiskySexualBehavior(entity.getHasRiskySexualBehavior());
        dto.setIsEligible(entity.getIsEligible());
        dto.setNotesByStaff(entity.getNotesByStaff());

        // ✅ Mapping thông số xét nghiệm máu
        dto.setHemoglobin(entity.getHemoglobin());
        dto.setHbsAgPositive(entity.getHbsAgPositive());
        dto.setHcvPositive(entity.getHcvPositive());
        dto.setHivPositive(entity.getHivPositive());
        dto.setSyphilisPositive(entity.getSyphilisPositive());

        return dto;
    }
}
