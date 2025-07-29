package com.quyet.superapp.service;

import com.quyet.superapp.dto.HealthCheckFormDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.enums.HealthCheckFailureReason;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.mapper.HealthCheckFormMapper;
import com.quyet.superapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthCheckFormService {

    private final HealthCheckFormRepository formRepository;
    private final DonationRegistrationRepository registrationRepository;
    private final HealthCheckFailureLogService failureLogService;
    private final UserProfileRepository userProfileRepository;
    private final DonationRepository donationRepository;
    private final HealthCheckFormMapper healthCheckFormMapper;

    public HealthCheckFormDTO submit(HealthCheckFormDTO dto) {
        if (formRepository.existsByRegistration_RegistrationId(dto.getRegistrationId())) {
            throw new MemberException("FORM_EXISTS", "Phiếu khám cho đơn này đã tồn tại.");
        }

        DonationRegistration reg = getRegistrationOrThrow(dto.getRegistrationId());
        boolean isEligible = evaluate(dto);

        HealthCheckForm form = healthCheckFormMapper.toEntity(dto, reg, isEligible);
        formRepository.save(form);

        updateUserProfile(reg.getUser().getUserProfile(), dto);
        dto.setIsEligible(isEligible);

        if (!isEligible) {
            markRegistrationFailed(reg, dto);
        } else {
            Donation donation = createDonation(reg);
            form.setDonation(donation);
            formRepository.save(form); // update form with donation
        }

        return dto;
    }

    public HealthCheckFormDTO getByRegistrationId(Long regId) {
        HealthCheckForm form = formRepository.findByRegistration_RegistrationId(regId);
        if (form == null) {
            throw new MemberException("NOT_FOUND", "Chưa có phiếu khám.");
        }
        return healthCheckFormMapper.toDTO(form);
    }

    public List<HealthCheckFormDTO> getAll() {
        return formRepository.findAll().stream()
                .map(healthCheckFormMapper::toDTO)
                .collect(Collectors.toList());
    }

    public HealthCheckFormDTO update(HealthCheckFormDTO dto) {
        HealthCheckForm form = formRepository.findByRegistration_RegistrationId(dto.getRegistrationId());
        if (form == null) {
            throw new MemberException("NOT_FOUND", "Chưa có phiếu khám để cập nhật.");
        }

        DonationRegistration reg = form.getRegistration();
        updateUserProfile(reg.getUser().getUserProfile(), dto);

        copyDtoToForm(dto, form);
        boolean eligible = evaluate(dto);
        form.setIsEligible(eligible);

        formRepository.save(form);
        reg.setStatus(eligible ? DonationStatus.PASSED_HEALTH : DonationStatus.FAILED_HEALTH);
        registrationRepository.save(reg);

        dto.setIsEligible(eligible);
        return dto;
    }

    // ====== PRIVATE SUPPORT METHODS ======

    private void copyDtoToForm(HealthCheckFormDTO dto, HealthCheckForm form) {
        form.setWeightKg(dto.getWeightKg());
        form.setHeightCm(dto.getHeightCm());
        form.setBloodPressureSys(dto.getBloodPressureSys());
        form.setBloodPressureDia(dto.getBloodPressureDia());
        form.setHeartRate(dto.getHeartRate());
        form.setBodyTemperature(dto.getBodyTemperature());
        form.setHasFever(dto.getHasFever());
        form.setTookAntibioticsRecently(dto.getTookAntibioticsRecently());
        form.setHasChronicIllness(dto.getHasChronicIllness());
        form.setIsPregnantOrBreastfeeding(dto.getIsPregnantOrBreastfeeding());
        form.setHadRecentTattooOrSurgery(dto.getHadRecentTattooOrSurgery());
        form.setHasRiskySexualBehavior(dto.getHasRiskySexualBehavior());
        form.setHemoglobin(dto.getHemoglobin());
        form.setHbsAgPositive(dto.getHbsAgPositive());
        form.setHcvPositive(dto.getHcvPositive());
        form.setHivPositive(dto.getHivPositive());
        form.setSyphilisPositive(dto.getSyphilisPositive());
        form.setNotesByStaff(dto.getNotesByStaff());
    }

    private void updateUserProfile(UserProfile profile, HealthCheckFormDTO dto) {
        if (profile != null) {
            profile.setWeightKg(dto.getWeightKg());
            profile.setHeightCm(dto.getHeightCm());
            userProfileRepository.save(profile);
        }
    }

    private void markRegistrationFailed(DonationRegistration reg, HealthCheckFormDTO dto) {
        reg.setStatus(DonationStatus.FAILED_HEALTH);
        registrationRepository.save(reg);

        failureLogService.saveLog(
                reg.getRegistrationId(),
                HealthCheckFailureReason.OTHER,
                dto.getNotesByStaff() != null ? dto.getNotesByStaff() : "Không có ghi chú"
        );
    }

    private Donation createDonation(DonationRegistration reg) {
        return donationRepository.save(
                Donation.builder()
                        .registration(reg)
                        .user(reg.getUser())
                        .collectedAt(LocalDate.now())
                        .volumeMl(450)
                        .bloodType(reg.getBloodType())
                        .status(DonationStatus.CONFIRMED)
                        .build()
        );
    }

    private DonationRegistration getRegistrationOrThrow(Long regId) {
        return registrationRepository.findById(regId)
                .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy đơn đăng ký."));
    }

    private boolean evaluate(HealthCheckFormDTO dto) {
        return isVitalsOK(dto) && isBehaviorOK(dto) && isTestOK(dto);
    }

    private boolean isVitalsOK(HealthCheckFormDTO dto) {
        return dto.getBodyTemperature() >= 36 && dto.getBodyTemperature() <= 37.5
                && dto.getHeartRate() >= 60 && dto.getHeartRate() <= 100
                && dto.getBloodPressureSys() <= 140 && dto.getBloodPressureDia() <= 90
                && dto.getWeightKg() >= 42;
    }

    private boolean isBehaviorOK(HealthCheckFormDTO dto) {
        return !(Boolean.TRUE.equals(dto.getHasFever())
                || Boolean.TRUE.equals(dto.getTookAntibioticsRecently())
                || Boolean.TRUE.equals(dto.getHasChronicIllness())
                || Boolean.TRUE.equals(dto.getIsPregnantOrBreastfeeding())
                || Boolean.TRUE.equals(dto.getHadRecentTattooOrSurgery())
                || Boolean.TRUE.equals(dto.getHasRiskySexualBehavior()));
    }

    private boolean isTestOK(HealthCheckFormDTO dto) {
        return (dto.getHemoglobin() == null || dto.getHemoglobin() >= 12.5)
                && !Boolean.TRUE.equals(dto.getHbsAgPositive())
                && !Boolean.TRUE.equals(dto.getHcvPositive())
                && !Boolean.TRUE.equals(dto.getHivPositive())
                && !Boolean.TRUE.equals(dto.getSyphilisPositive());
    }
}
