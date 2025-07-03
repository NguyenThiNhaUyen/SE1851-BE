    package com.quyet.superapp.service;

    import com.quyet.superapp.dto.FullHealthCheckDTO;
    import com.quyet.superapp.dto.HealthCheckFormDTO;
    import com.quyet.superapp.dto.PreDonationTestDTO;
    import com.quyet.superapp.entity.Donation;
    import com.quyet.superapp.entity.DonationRegistration;
    import com.quyet.superapp.entity.HealthCheckForm;
    import com.quyet.superapp.entity.UserProfile;
    import com.quyet.superapp.enums.DonationStatus;
    import com.quyet.superapp.exception.MemberException;
    import com.quyet.superapp.mapper.HealthCheckFormMapper;
    import com.quyet.superapp.mapper.PreDonationTestMapper;
    import com.quyet.superapp.repository.DonationRegistrationRepository;
    import com.quyet.superapp.repository.DonationRepository;
    import com.quyet.superapp.repository.HealthCheckFormRepository;
    import com.quyet.superapp.repository.UserProfileRepository;
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
        private final PreDonationTestMapper preDonationTestMapper;

        public HealthCheckFormDTO submit(HealthCheckFormDTO dto) {
            if (formRepository.existsByRegistration_RegistrationId(dto.getRegistrationId())) {
                throw new MemberException("FORM_EXISTS", "Phiếu khám cho đơn này đã tồn tại.");
            }

            DonationRegistration reg = registrationRepository.findById(dto.getRegistrationId())
                    .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy đơn đăng ký."));

            boolean isEligible = evaluate(dto);

            HealthCheckForm entity = healthCheckFormMapper.toEntity(dto, reg, isEligible);
            formRepository.save(entity);

            // ✅ Cập nhật cân nặng / chiều cao người hiến
            UserProfile profile = reg.getUser().getUserProfile();
            if (profile != null) {
                profile.setWeightKg(dto.getWeightKg());
                profile.setHeightCm(dto.getHeightCm());
                userProfileRepository.save(profile);
            }

            dto.setIsEligible(isEligible);

            if (!isEligible) {
                reg.setStatus(DonationStatus.FAILED_HEALTH);
                registrationRepository.save(reg);

                // Ghi log nếu không đủ điều kiện
                failureLogService.saveLog(
                        reg.getRegistrationId(),
                        "Không đạt yêu cầu sức khỏe dựa trên thông số",
                        dto.getNotesByStaff() != null ? dto.getNotesByStaff() : "Không có ghi chú"
                );
            } else {
                // ✅ Nếu đạt sức khỏe thì tạo Donation luôn
                Donation donation = Donation.builder()
                        .registration(reg)
                        .user(reg.getUser())
                        .donationDate(LocalDate.now())
                        .status(DonationStatus.CONFIRMED)
                        .build();

                donation = donationRepository.save(donation);

                // Gắn vào phiếu khám
                entity.setDonation(donation);
                formRepository.save(entity); // update form để gắn donation
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

            DonationRegistration reg = form.getRegistration();

            UserProfile profile = reg.getUser().getUserProfile();
            if (profile != null) {
                profile.setWeightKg(dto.getWeightKg());
                profile.setHeightCm(dto.getHeightCm());
                userProfileRepository.save(profile);
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
            if (!eligible) {
                reg.setStatus(DonationStatus.FAILED_HEALTH);
            } else {
                reg.setStatus(DonationStatus.CONFIRMED); // hoặc để nguyên tùy nghiệp vụ
            }
            registrationRepository.save(reg);

            dto.setIsEligible(eligible);
            return dto;
        }
        public FullHealthCheckDTO getFullByRegistrationId(Long regId) {
            HealthCheckForm form = formRepository.findByRegistration_RegistrationId(regId);
            if (form == null) {
                throw new MemberException("NOT_FOUND", "Chưa có phiếu khám.");
            }
            HealthCheckFormDTO healthDTO = healthCheckFormMapper.toDTO(form);
            PreDonationTestDTO testDTO = null;

            if (form.getPreDonationTest() != null) {
                testDTO = preDonationTestMapper.toDTO(form.getPreDonationTest());
            }

            return new FullHealthCheckDTO(healthDTO, testDTO);
        }


    }
