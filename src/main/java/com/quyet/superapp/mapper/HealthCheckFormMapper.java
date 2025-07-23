package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.HealthCheckFormDTO;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.HealthCheckForm;
<<<<<<< HEAD

public class HealthCheckFormMapper {
    public static HealthCheckForm toEntity(HealthCheckFormDTO dto, DonationRegistration reg, boolean isEligible) {
=======
import org.springframework.stereotype.Component;

@Component
public class HealthCheckFormMapper {

    public HealthCheckForm toEntity(HealthCheckFormDTO dto, DonationRegistration reg, boolean isEligible) {
>>>>>>> origin/main
        return HealthCheckForm.builder()
                .registration(reg)
                .bodyTemperature(dto.getBodyTemperature())
                .heartRate(dto.getHeartRate())
                .bloodPressureSys(dto.getBloodPressureSys())
                .bloodPressureDia(dto.getBloodPressureDia())
                .weightKg(dto.getWeightKg())
<<<<<<< HEAD
=======
                .heightCm(dto.getHeightCm())
>>>>>>> origin/main
                .hasFever(dto.getHasFever())
                .tookAntibioticsRecently(dto.getTookAntibioticsRecently())
                .hasChronicIllness(dto.getHasChronicIllness())
                .isPregnantOrBreastfeeding(dto.getIsPregnantOrBreastfeeding())
                .hadRecentTattooOrSurgery(dto.getHadRecentTattooOrSurgery())
                .hasRiskySexualBehavior(dto.getHasRiskySexualBehavior())
                .isEligible(isEligible)
                .notesByStaff(dto.getNotesByStaff())
<<<<<<< HEAD
                .build();
    }

    public static HealthCheckFormDTO toDTO(HealthCheckForm entity) {
=======

                // ✅ Gộp thêm thông số xét nghiệm máu
                .hemoglobin(dto.getHemoglobin())
                .hbsAgPositive(dto.getHbsAgPositive())
                .hcvPositive(dto.getHcvPositive())
                .hivPositive(dto.getHivPositive())
                .syphilisPositive(dto.getSyphilisPositive())
                .build();
    }

    public HealthCheckFormDTO toDTO(HealthCheckForm entity) {
>>>>>>> origin/main
        HealthCheckFormDTO dto = new HealthCheckFormDTO();
        dto.setId(entity.getId());
        dto.setRegistrationId(entity.getRegistration().getRegistrationId());
        dto.setBodyTemperature(entity.getBodyTemperature());
        dto.setHeartRate(entity.getHeartRate());
        dto.setBloodPressureSys(entity.getBloodPressureSys());
        dto.setBloodPressureDia(entity.getBloodPressureDia());
        dto.setWeightKg(entity.getWeightKg());
<<<<<<< HEAD
=======
        dto.setHeightCm(entity.getHeightCm());
>>>>>>> origin/main
        dto.setHasFever(entity.getHasFever());
        dto.setTookAntibioticsRecently(entity.getTookAntibioticsRecently());
        dto.setHasChronicIllness(entity.getHasChronicIllness());
        dto.setIsPregnantOrBreastfeeding(entity.getIsPregnantOrBreastfeeding());
        dto.setHadRecentTattooOrSurgery(entity.getHadRecentTattooOrSurgery());
        dto.setHasRiskySexualBehavior(entity.getHasRiskySexualBehavior());
        dto.setIsEligible(entity.getIsEligible());
        dto.setNotesByStaff(entity.getNotesByStaff());
<<<<<<< HEAD
        return dto;
    }

=======

        // ✅ Mapping thông số xét nghiệm máu
        dto.setHemoglobin(entity.getHemoglobin());
        dto.setHbsAgPositive(entity.getHbsAgPositive());
        dto.setHcvPositive(entity.getHcvPositive());
        dto.setHivPositive(entity.getHivPositive());
        dto.setSyphilisPositive(entity.getSyphilisPositive());

        return dto;
    }
>>>>>>> origin/main
}
