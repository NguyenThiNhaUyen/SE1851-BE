package com.quyet.superapp.service;

import com.quyet.superapp.dto.HealthCheckFailureLogDTO;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.HealthCheckFailureLog;
import com.quyet.superapp.mapper.HealthCheckFailureLogMapper;
import com.quyet.superapp.repository.DonationRegistrationRepository;
import com.quyet.superapp.repository.HealthCheckFailureLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthCheckFailureLogService {
    private final HealthCheckFailureLogRepository logRepository;
    private final DonationRegistrationRepository registrationRepository;

    public HealthCheckFailureLogDTO createLog(Long registrationId, String reason, String staffNote) {
        DonationRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đăng ký với ID: " + registrationId));

        HealthCheckFailureLog log = new HealthCheckFailureLog();
        log.setRegistration(registration);
        log.setReason(reason);
        log.setStaffNote(staffNote);

        return HealthCheckFailureLogMapper.toDTO(logRepository.save(log));
    }

    public List<HealthCheckFailureLogDTO> getLogsByRegistrationId(Long registrationId) {
        return logRepository.findByRegistration_RegistrationId(registrationId).stream()
                .map(HealthCheckFailureLogMapper::toDTO)
                .collect(Collectors.toList());
    }

}
