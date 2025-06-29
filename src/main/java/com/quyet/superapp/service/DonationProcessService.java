package com.quyet.superapp.service;

import com.quyet.superapp.dto.FullDonationProcessRequest;
import com.quyet.superapp.dto.HealthCheckFormDTO;
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.HealthCheckForm;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.mapper.HealthCheckFormMapper;
import com.quyet.superapp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DonationProcessService {
    private final DonationRegistrationRepository registrationRepository;
    private final HealthCheckFormRepository healthCheckFormRepository;
    private final DonationRepository donationRepository;
    private final HealthCheckFormMapper healthCheckFormMapper;

    @Transactional
    public void handleFullDonationProcess(FullDonationProcessRequest request) {
        Long regId = request.getRegistrationId();

        DonationRegistration reg = registrationRepository.findById(regId)
                .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy đơn đăng ký"));

        // 1. Khám sức khỏe
        HealthCheckFormDTO healthDTO = request.getHealthCheckFormDTO();
        boolean eligible = evaluate(healthDTO);

        HealthCheckForm healthForm = healthCheckFormMapper.toEntity(healthDTO, reg, eligible);
        healthCheckFormRepository.save(healthForm);

        if (!eligible) {
            reg.setStatus(DonationStatus.FAILED_HEALTH);
            registrationRepository.save(reg);
            throw new MemberException("HEALTH_FAIL", "Không đủ điều kiện hiến máu.");
        }

        // 2. Tạo bản ghi Donation (nếu đủ điều kiện)
        Donation donation = Donation.builder()
                .registration(reg)
                .user(reg.getUser())
                .donationDate(LocalDate.now())
                .status(DonationStatus.CONFIRMED)
                .build();
        donation = donationRepository.save(donation);

        // Gắn donation vào health form
        healthForm.setDonation(donation);
        healthCheckFormRepository.save(healthForm);

        // 3. Cập nhật trạng thái đơn đăng ký
        reg.setStatus(DonationStatus.CONFIRMED);
        registrationRepository.save(reg);
    }

    private boolean evaluate(HealthCheckFormDTO dto) {
        if (dto.getBodyTemperature() < 36 || dto.getBodyTemperature() > 37.5) return false;
        if (dto.getHeartRate() < 60 || dto.getHeartRate() > 100) return false;
        if (dto.getBloodPressureSys() > 140 || dto.getBloodPressureDia() > 90) return false;
        if (dto.getWeightKg() < 42) return false;
        return !(dto.getHasFever() || dto.getTookAntibioticsRecently()
                || dto.getHasChronicIllness() || dto.getIsPregnantOrBreastfeeding()
                || dto.getHadRecentTattooOrSurgery() || dto.getHasRiskySexualBehavior());
    }
}
