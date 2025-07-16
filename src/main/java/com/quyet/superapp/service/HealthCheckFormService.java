package com.quyet.superapp.service;

import com.quyet.superapp.dto.HealthCheckFormDTO;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.HealthCheckForm;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.mapper.HealthCheckFormMapper;
import com.quyet.superapp.repository.DonationRegistrationRepository;
import com.quyet.superapp.repository.HealthCheckFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthCheckFormService {

    private final HealthCheckFormRepository formRepository;
    private final DonationRegistrationRepository registrationRepository;
    private final HealthCheckFailureLogService failureLogService;

    public HealthCheckFormDTO submit(HealthCheckFormDTO dto) {
        if (formRepository.existsByRegistration_RegistrationId(dto.getRegistrationId())) {
            throw new MemberException("FORM_EXISTS", "Phiếu khám cho đơn này đã tồn tại.");
        }

        DonationRegistration reg = registrationRepository.findById(dto.getRegistrationId())
                .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy đơn đăng ký."));

        boolean isEligible = evaluate(dto);

        HealthCheckForm entity = HealthCheckFormMapper.toEntity(dto, reg, isEligible);
        formRepository.save(entity);

        dto.setIsEligible(isEligible);

        if (!isEligible) {
            reg.setStatus(DonationStatus.CONFIRMED);
            registrationRepository.save(reg);

            // ✅ Ghi log nếu không đủ điều kiện
            failureLogService.saveLog(
                    reg.getRegistrationId(),
                    "Không đạt yêu cầu sức khỏe dựa trên thông số",
                    dto.getNotesByStaff() != null ? dto.getNotesByStaff() : "Không có ghi chú"
            );
        }

        return dto;
    }

    public HealthCheckFormDTO getByRegistrationId(Long regId) {
        HealthCheckForm form = formRepository.findByRegistration_RegistrationId(regId);
        if (form == null) {
            throw new MemberException("NOT_FOUND", "Chưa có phiếu khám.");
        }
        return HealthCheckFormMapper.toDTO(form);
    }

    public List<HealthCheckFormDTO> getAll() {
        return formRepository.findAll().stream()
                .map(HealthCheckFormMapper::toDTO)
                .collect(Collectors.toList());
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

    public HealthCheckFormDTO update(HealthCheckFormDTO dto) {
        HealthCheckForm form = formRepository.findByRegistration_RegistrationId(dto.getRegistrationId());
        if (form == null) {
            throw new MemberException("NOT_FOUND", "Chưa có phiếu khám để cập nhật.");
        }

        // Cập nhật các trường mới từ DTO
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

        // Đánh giá lại điều kiện
        boolean eligible = evaluate(dto);
        form.setIsEligible(eligible);

        formRepository.save(form);

        // Cập nhật lại trạng thái đơn nếu cần
        DonationRegistration reg = form.getRegistration();
        if (!eligible) {
            reg.setStatus(DonationStatus.CONFIRMED);
        } else {
            reg.setStatus(DonationStatus.CONFIRMED); // hoặc để nguyên tùy nghiệp vụ
        }
        registrationRepository.save(reg);

        dto.setIsEligible(eligible);
        return dto;
    }
    public HealthCheckFormDTO getOrCreateByRegistrationId(Long regId) {
        HealthCheckForm existing = formRepository.findByRegistration_RegistrationId(regId);
        if (existing != null) {
            return HealthCheckFormMapper.toDTO(existing);
        }

        // Nếu chưa có phiếu, tạo mới
        DonationRegistration reg = registrationRepository.findById(regId)
                .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy đơn đăng ký."));

        HealthCheckForm form = new HealthCheckForm();
        form.setRegistration(reg);
        form.setWeightKg(null);
        form.setBloodPressureSys(null);
        form.setBloodPressureDia(null);
        form.setHeartRate(null);
        form.setIsEligible(null);
        form.setHasFever(false);
        form.setHasChronicIllness(false);
        form.setTookAntibioticsRecently(false);
        form.setHadRecentTattooOrSurgery(false);
        form.setHasRiskySexualBehavior(false);
        form.setIsPregnantOrBreastfeeding(false);
        form.setBodyTemperature(null);
        form.setNotesByStaff(null);

        HealthCheckForm saved = formRepository.save(form);
        return HealthCheckFormMapper.toDTO(saved);
    }


}
