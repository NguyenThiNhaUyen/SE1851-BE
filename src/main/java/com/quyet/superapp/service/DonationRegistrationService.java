package com.quyet.superapp.service;

import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.mapper.DonationRegistrationMapper;
import com.quyet.superapp.repository.*;
import com.quyet.superapp.repository.address.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationRegistrationService {

    private final DonationRegistrationRepository donationRegistrationRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final EmailService emailService;
    private final AddressRepository addressRepository;
    private final DonationRepository donationRepository;
    private final BloodTypeRepository bloodTypeRepository;
    private final HealthCheckFormRepository healthCheckFormRepository;
    private final HealthCheckFailureLogService healthCheckFailureLogService;
    private final DonationRegistrationMapper donationRegistrationMapper;

    // ✅ Đăng ký hiến máu
    public DonationRegistrationDTO register(Long userId, DonationRegistrationDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy User với ID: " + userId));

        // Kiểm tra trùng đăng ký (đang chờ xác nhận)
        if (donationRegistrationRepository.existsByUser_UserIdAndStatus(userId, DonationStatus.PENDING)) {
            throw new MemberException("DUPLICATE_PENDING", "Bạn đã có một đơn đăng ký đang chờ xác nhận.");
        }

        // Tạo hoặc cập nhật UserProfile nếu chưa có
        if (user.getUserProfile() == null) {
            UserProfile profile = new UserProfile();
            profile.setUser(user);
            profile.setFullName(dto.getFullName());
            profile.setDob(dto.getDob());
            profile.setGender(dto.getGender());
            profile.setBloodType(dto.getBloodType());
            if (dto.getAddressId() != null) {
                Address address = addressRepository.findById(dto.getAddressId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ với ID: " + dto.getAddressId()));
                profile.setAddress(address);
            }
            profile.setPhone(dto.getPhone());
            userProfileRepository.save(profile);
        }

        // Tạo đơn đăng ký mới
        DonationRegistration registration = donationRegistrationMapper.toEntity(dto, user);
        registration.setStatus(DonationStatus.PENDING);

        return donationRegistrationMapper.toDTO(donationRegistrationRepository.save(registration));
    }

    // ✅ Lấy danh sách tất cả đơn đăng ký
    public List<DonationRegistrationDTO> getAllDTO() {
        return donationRegistrationRepository.findAllWithDetails().stream()
                .map(donationRegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Lấy đơn đăng ký theo ID
    public DonationRegistrationDTO getDTOById(Long id) {
        return donationRegistrationRepository.findById(id)
                .map(donationRegistrationMapper::toDTO)
                .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy đơn đăng ký"));
    }

    public List<DonationRegistrationDTO> getByUserId(Long userId) {
        List<DonationRegistration> list = donationRegistrationRepository.findByUser_UserId(userId);
        return list.stream().map(donationRegistrationMapper::toDTO).toList();
    }

    // ✅ Xác nhận đơn đăng ký
    public DonationRegistrationDTO confirm(Long registrationId) {
        DonationRegistration reg = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("REGISTRATION_NOT_FOUND",
                        "Không tìm thấy đơn đăng ký có ID = " + registrationId));

        if (reg.getStatus() != DonationStatus.PENDING) {
            throw new MemberException("INVALID_STATUS", "Đơn đăng ký không ở trạng thái chờ xác nhận.");
        }

        reg.setStatus(DonationStatus.CONFIRMED);
        donationRegistrationRepository.save(reg);

        // ✅ Gửi email xác nhận
        String subject = "Xác nhận hiến máu thành công";
        String content = String.format(
                "<h3>Chào %s</h3><p>Đơn đăng ký hiến máu của bạn vào ngày <b>%s</b> đã được xác nhận.</p><p>Cảm ơn bạn vì nghĩa cử cao đẹp!</p>",
                reg.getUser().getUsername(), reg.getReadyDate());
        emailService.sendEmail(reg.getUser(), subject, content, "XÁC NHẬN");

        return donationRegistrationMapper.toDTO(reg);
    }

    // ✅ Tạo bản ghi hiến máu nếu đạt sức khỏe
    public Donation createDonationIfEligible(Long registrationId) {
        DonationRegistration reg = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy đơn đăng ký."));

        if (reg.getStatus() != DonationStatus.CONFIRMED) {
            throw new MemberException("INVALID_STATUS", "Chỉ tạo bản ghi hiến máu khi đơn đã được xác nhận.");
        }

        // Kiểm tra phiếu khám
        HealthCheckForm form = healthCheckFormRepository.findByRegistration_RegistrationId(registrationId);
        if (form == null || !Boolean.TRUE.equals(form.getIsEligible())) {
            throw new MemberException("NOT_ELIGIBLE", "Người hiến máu không đạt yêu cầu sức khỏe.");
        }

        // Tránh trùng bản ghi
        if (donationRepository.existsByRegistration_RegistrationId(registrationId)) {
            throw new MemberException("DUPLICATE_DONATION", "Đã tồn tại bản ghi hiến máu cho đơn này.");
        }

        BloodType bloodType = bloodTypeRepository.findByDescription(reg.getBloodType())
                .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy nhóm máu: " + reg.getBloodType()));

        Donation donation = new Donation();
        donation.setUser(reg.getUser());
        donation.setRegistration(reg);
        donation.setVolumeMl(450);
        donation.setBloodType(bloodType);
        donation.setDonationDate(reg.getReadyDate());

        donationRepository.save(donation);

        // Cập nhật hồ sơ
        UserProfile profile = reg.getUser().getUserProfile();
        profile.setLastDonationDate(reg.getReadyDate());
        profile.setRecoveryTime(90);
        userProfileRepository.save(profile);

        // ✅ Cập nhật trạng thái: đã hiến máu thành công
        reg.setStatus(DonationStatus.DONATED);
        donationRegistrationRepository.save(reg);

        return donation;
    }

    // ✅ Lọc đơn theo trạng thái
    public List<DonationRegistrationDTO> getByStatus(DonationStatus status) {
        return donationRegistrationRepository.findByStatus(status).stream()
                .map(donationRegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Đánh dấu đơn là HỦY (người hiến không đến)
    public DonationRegistrationDTO markAsCancelled(Long registrationId) {
        DonationRegistration reg = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("REGISTRATION_NOT_FOUND",
                        "Không tìm thấy đơn đăng ký có ID = " + registrationId));

        if (reg.getStatus() != DonationStatus.CONFIRMED) {
            throw new MemberException("INVALID_STATUS", "Chỉ có thể hủy đơn đã xác nhận.");
        }

        reg.setStatus(DonationStatus.CANCELLED);
        donationRegistrationRepository.save(reg);
        return donationRegistrationMapper.toDTO(reg);
    }

    // ✅ Đánh dấu KHÔNG ĐẠT sức khỏe
    public DonationRegistrationDTO markAsFailedHealth(Long registrationId, String reason, String staffNote) {
        DonationRegistration reg = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("REGISTRATION_NOT_FOUND", "Không tìm thấy đơn đăng ký có ID = " + registrationId));

        if (reg.getStatus() != DonationStatus.CONFIRMED) {
            throw new MemberException("INVALID_STATUS", "Chỉ đánh dấu không đạt sức khỏe khi đơn đã được xác nhận.");
        }

        reg.setStatus(DonationStatus.FAILED_HEALTH);
        donationRegistrationRepository.save(reg);

        // ✅ Ghi log
        healthCheckFailureLogService.saveLog(registrationId, reason, staffNote);

        return donationRegistrationMapper.toDTO(reg);
    }

    public DonationRegistrationDTO markAsDonated(Long registrationId) {
        DonationRegistration reg = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("REGISTRATION_NOT_FOUND",
                        "Không tìm thấy đơn đăng ký có ID = " + registrationId));

        if (reg.getStatus() != DonationStatus.CONFIRMED) {
            throw new MemberException("INVALID_STATUS", "Chỉ những đơn đã xác nhận mới được đánh dấu là đã hiến.");
        }

        reg.setStatus(DonationStatus.DONATED);
        DonationRegistration saved = donationRegistrationRepository.save(reg);

        return donationRegistrationMapper.toDTO(saved);
    }

    public DonationRegistrationDTO cancel(Long registrationId) {
        DonationRegistration reg = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("REGISTRATION_NOT_FOUND",
                        "Không tìm thấy đơn đăng ký có ID = " + registrationId));

        if (reg.getStatus() == DonationStatus.CANCELLED || reg.getStatus() == DonationStatus.DONATED) {
            throw new MemberException("INVALID_STATUS", "Không thể hủy đơn ở trạng thái hiện tại.");
        }

        reg.setStatus(DonationStatus.CANCELLED);
        return donationRegistrationMapper.toDTO(donationRegistrationRepository.save(reg));
    }
}
