package com.quyet.superapp.service;

import com.quyet.superapp.dto.HealthCheckFormDTO;
<<<<<<< HEAD
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.HealthCheckForm;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.mapper.HealthCheckFormMapper;
import com.quyet.superapp.repository.DonationRegistrationRepository;
import com.quyet.superapp.repository.HealthCheckFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

=======
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.HealthCheckForm;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.enums.HealthCheckFailureReason;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.mapper.HealthCheckFormMapper;
import com.quyet.superapp.repository.DonationRegistrationRepository;
import com.quyet.superapp.repository.DonationRepository;
import com.quyet.superapp.repository.HealthCheckFormRepository;
import com.quyet.superapp.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
>>>>>>> origin/main
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthCheckFormService {

    private final HealthCheckFormRepository formRepository;
    private final DonationRegistrationRepository registrationRepository;
    private final HealthCheckFailureLogService failureLogService;
<<<<<<< HEAD
=======
    private final UserProfileRepository userProfileRepository;
    private final DonationRepository donationRepository;
    private final HealthCheckFormMapper healthCheckFormMapper;
>>>>>>> origin/main

    public HealthCheckFormDTO submit(HealthCheckFormDTO dto) {
        if (formRepository.existsByRegistration_RegistrationId(dto.getRegistrationId())) {
            throw new MemberException("FORM_EXISTS", "Phiếu khám cho đơn này đã tồn tại.");
        }

        DonationRegistration reg = registrationRepository.findById(dto.getRegistrationId())
                .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy đơn đăng ký."));

        boolean isEligible = evaluate(dto);

<<<<<<< HEAD
        HealthCheckForm entity = HealthCheckFormMapper.toEntity(dto, reg, isEligible);
        formRepository.save(entity);

=======
        HealthCheckForm entity = healthCheckFormMapper.toEntity(dto, reg, isEligible);
        formRepository.save(entity);

        UserProfile profile = reg.getUser().getUserProfile();
        if (profile != null) {
            profile.setWeightKg(dto.getWeightKg());
            profile.setHeightCm(dto.getHeightCm());
            userProfileRepository.save(profile);
        }

>>>>>>> origin/main
        dto.setIsEligible(isEligible);

        if (!isEligible) {
            reg.setStatus(DonationStatus.FAILED_HEALTH);
            registrationRepository.save(reg);

<<<<<<< HEAD
            // ✅ Ghi log nếu không đủ điều kiện
            failureLogService.saveLog(
                    reg.getRegistrationId(),
                    "Không đạt yêu cầu sức khỏe dựa trên thông số",
                    dto.getNotesByStaff() != null ? dto.getNotesByStaff() : "Không có ghi chú"
            );
=======
            failureLogService.saveLog(
                    reg.getRegistrationId(),
                    HealthCheckFailureReason.OTHER,
                    dto.getNotesByStaff() != null ? dto.getNotesByStaff() : "Không có ghi chú"
            );
        } else {
            Donation donation = Donation.builder()
                    .registration(reg)
                    .user(reg.getUser())
                    .collectedAt(LocalDate.now())
                    .volumeMl(450)
                    .bloodType(reg.getBloodType())
                    .status(DonationStatus.CONFIRMED)
                    .build();

            donation = donationRepository.save(donation);
            entity.setDonation(donation);
            formRepository.save(entity);
>>>>>>> origin/main
        }

        return dto;
    }

    public HealthCheckFormDTO getByRegistrationId(Long regId) {
        HealthCheckForm form = formRepository.findByRegistration_RegistrationId(regId);
        if (form == null) {
            throw new MemberException("NOT_FOUND", "Chưa có phiếu khám.");
        }
<<<<<<< HEAD
        return HealthCheckFormMapper.toDTO(form);
=======
        return healthCheckFormMapper.toDTO(form);
>>>>>>> origin/main
    }

    public List<HealthCheckFormDTO> getAll() {
        return formRepository.findAll().stream()
<<<<<<< HEAD
                .map(HealthCheckFormMapper::toDTO)
=======
                .map(healthCheckFormMapper::toDTO)
>>>>>>> origin/main
                .collect(Collectors.toList());
    }

    private boolean evaluate(HealthCheckFormDTO dto) {
<<<<<<< HEAD
        if (dto.getBodyTemperature() < 36 || dto.getBodyTemperature() > 37.5) return false;
        if (dto.getHeartRate() < 60 || dto.getHeartRate() > 100) return false;
        if (dto.getBloodPressureSys() > 140 || dto.getBloodPressureDia() > 90) return false;
        if (dto.getWeightKg() < 42) return false;

        return !(dto.getHasFever() || dto.getTookAntibioticsRecently()
                || dto.getHasChronicIllness() || dto.getIsPregnantOrBreastfeeding()
                || dto.getHadRecentTattooOrSurgery() || dto.getHasRiskySexualBehavior());
=======
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
>>>>>>> origin/main
    }

    public HealthCheckFormDTO update(HealthCheckFormDTO dto) {
        HealthCheckForm form = formRepository.findByRegistration_RegistrationId(dto.getRegistrationId());
        if (form == null) {
            throw new MemberException("NOT_FOUND", "Chưa có phiếu khám để cập nhật.");
        }

<<<<<<< HEAD
        // Cập nhật các trường mới từ DTO
=======
        DonationRegistration reg = form.getRegistration();

        UserProfile profile = reg.getUser().getUserProfile();
        if (profile != null) {
            profile.setWeightKg(dto.getWeightKg());
            profile.setHeightCm(dto.getHeightCm());
            userProfileRepository.save(profile);
        }

>>>>>>> origin/main
        form.setWeightKg(dto.getWeightKg());
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
<<<<<<< HEAD

        // Đánh giá lại điều kiện
=======
        form.setHemoglobin(dto.getHemoglobin());
        form.setHbsAgPositive(dto.getHbsAgPositive());
        form.setHcvPositive(dto.getHcvPositive());
        form.setHivPositive(dto.getHivPositive());
        form.setSyphilisPositive(dto.getSyphilisPositive());

>>>>>>> origin/main
        boolean eligible = evaluate(dto);
        form.setIsEligible(eligible);

        formRepository.save(form);

<<<<<<< HEAD
        // Cập nhật lại trạng thái đơn nếu cần
        DonationRegistration reg = form.getRegistration();
        if (!eligible) {
            reg.setStatus(DonationStatus.FAILED_HEALTH);
        } else {
            reg.setStatus(DonationStatus.CONFIRMED); // hoặc để nguyên tùy nghiệp vụ
=======
        if (!eligible) {
            reg.setStatus(DonationStatus.FAILED_HEALTH);
        } else {
            reg.setStatus(DonationStatus.PASSED_HEALTH);
>>>>>>> origin/main
        }
        registrationRepository.save(reg);

        dto.setIsEligible(eligible);
        return dto;
    }
<<<<<<< HEAD

=======
>>>>>>> origin/main
}
