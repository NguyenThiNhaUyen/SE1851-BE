package com.quyet.superapp.service;

import com.quyet.superapp.config.jwt.UserPrincipal;
import com.quyet.superapp.constant.MessageConstants;
import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.enums.HealthCheckFailureReason;
import com.quyet.superapp.event.EmailNotificationEvent;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.mapper.DonationRegistrationMapper;
import com.quyet.superapp.repository.*;
import com.quyet.superapp.validator.DonationRegistrationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonationRegistrationService {

    private final DonationRegistrationRepository registrationRepo;
    private final UserRepository userRepo;
    private final DonationRepository donationRepo;
    private final HealthCheckFailureLogService failureLogService;
    private final ApplicationEventPublisher eventPublisher;
    private final DonationRegistrationValidator validator;
    private final DonationSlotService slotService;
    private final BloodBagService bloodBagService;
    private final RecoveryReminderService recoveryReminderService;
    private final EmailService emailService;
    private final UserProfileService userProfileService;

    // ✅ Xử lý đăng ký hiến máu từ phía người dùng
    public ResponseEntity<?> register(Long userId, DonationRegistrationDTO dto) {
        User user = findUser(userId);
        validator.validateRegistrationRequest(user, dto);

        userProfileService.updateOrCreateFromRegistration(user, dto);

        validateSlotAndAssign(dto, user);

        DonationRegistration reg = DonationRegistrationMapper.toEntity(dto, user);
        reg.setStatus(DonationStatus.PENDING);
        slotService.assignSlotToRegistration(reg, dto.getSlotId());

        registrationRepo.save(reg);
        emailService.sendPreDonationInstructionEmail(user, dto.getScheduledDate());

        return buildSuccess(DonationRegistrationMapper.toDTO(reg), MessageConstants.DONATION_REGISTERED);
    }

    // ✅ Xác nhận đơn hiến máu (STAFF/ADMIN)
    public ResponseEntity<?> confirm(Long regId, UserPrincipal principal) {
        User staff = findUser(principal.getUserId());
        ensureStaffPrivileges(staff);

        DonationRegistration reg = getValidRegistration(regId, DonationStatus.PENDING);
        reg.setStatus(DonationStatus.CONFIRMED);
        reg.setConfirmedBy(staff);
        reg.getSlot().decreaseAvailableCapacity();

        registrationRepo.save(reg);
        sendConfirmationEmail(reg);

        return buildSuccess(DonationRegistrationMapper.toDTO(reg), MessageConstants.DONATION_CONFIRMED);
    }

    // ✅ Đánh dấu đã hủy (có thể do người dùng hoặc hệ thống)
    public ResponseEntity<?> markAsCancelled(Long regId) {
        DonationRegistration reg = getValidRegistration(regId, DonationStatus.CONFIRMED);
        reg.setStatus(DonationStatus.CANCELLED);
        registrationRepo.save(reg);

        return buildSuccess(DonationRegistrationMapper.toDTO(reg), MessageConstants.DONATION_CANCELLED);
    }

    // ✅ Đánh dấu thất bại do sức khỏe
    public ResponseEntity<?> markAsFailedHealth(Long regId, HealthCheckFailureReason reason, String note) {
        DonationRegistration reg = getValidRegistration(regId, DonationStatus.CONFIRMED);
        reg.setStatus(DonationStatus.FAILED_HEALTH);
        registrationRepo.save(reg);

        failureLogService.saveLog(regId, reason, note);

        return buildSuccess(DonationRegistrationMapper.toDTO(reg), MessageConstants.DONATION_FAILED_HEALTH);
    }

    // ✅ Đánh dấu đã hiến máu và tạo túi máu nếu cần
    public ResponseEntity<?> markAsDonated(Long regId) {
        DonationRegistration reg = getValidRegistration(regId, DonationStatus.CONFIRMED);
        Donation donation = donationRepo.findByRegistration_RegistrationId(regId)
                .orElseThrow(() -> new MemberException("DONATION_NOT_FOUND", "Không tìm thấy thông tin hiến máu."));

        // Tạo túi máu nếu chưa có
        if (donation.getBloodBag() == null) {
            donation.setBloodBag(bloodBagService.createFromDonation(donation));
            donationRepo.save(donation);
        }

        reg.setStatus(DonationStatus.DONATED);
        registrationRepo.save(reg);
        recoveryReminderService.scheduleRecoveryReminder(donation);

        log.info("✅ Đã đánh dấu hiến máu thành công, regId={}", regId);
        return buildSuccess(DonationRegistrationMapper.toDTO(reg), "✅ DONATED thành công");
    }

    // ✅ Truy vấn danh sách đơn theo trạng thái
    public List<DonationRegistrationDTO> getByStatus(DonationStatus status) {
        return registrationRepo.findByStatus(status)
                .stream().map(DonationRegistrationMapper::toDTO).collect(Collectors.toList());
    }

    // ✅ Truy vấn tất cả đơn đăng ký
    public ResponseEntity<?> getAllDTO() {
        List<DonationRegistrationDTO> list = registrationRepo.findAll()
                .stream().map(DonationRegistrationMapper::toDTO).collect(Collectors.toList());
        return buildSuccess(list, MessageConstants.GET_REGISTRATION_SUCCESS);
    }

    // ✅ Lấy chi tiết theo ID
    public ResponseEntity<?> getDTOById(Long id) {
        DonationRegistration reg = getRegistrationOrThrow(id);
        return buildSuccess(DonationRegistrationMapper.toDTO(reg), MessageConstants.GET_REGISTRATION_SUCCESS);
    }

    // ✅ Truy vấn đơn theo slot
    public List<DonationRegistrationDTO> getBySlotId(Long slotId) {
        return registrationRepo.findBySlot_SlotId(slotId)
                .stream().map(DonationRegistrationMapper::toDTO).collect(Collectors.toList());
    }

    // ========== PRIVATE SUPPORT METHODS ==========

    private User findUser(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new MemberException("USER_NOT_FOUND", MessageConstants.USER_NOT_FOUND));
    }

    private DonationRegistration getValidRegistration(Long regId, DonationStatus requiredStatus) {
        DonationRegistration reg = getRegistrationOrThrow(regId);
        if (reg.getStatus() != requiredStatus) {
            throw new MemberException("INVALID_STATUS", MessageConstants.INVALID_REGISTRATION_STATUS);
        }
        return reg;
    }

    private DonationRegistration getRegistrationOrThrow(Long id) {
        return registrationRepo.findById(id)
                .orElseThrow(() -> new MemberException("REGISTRATION_NOT_FOUND", MessageConstants.DONATION_REGISTRATION_NOT_FOUND));
    }

    private void ensureStaffPrivileges(User user) {
        String role = user.getRole().getName();
        if (!role.equals("STAFF") && !role.equals("ADMIN")) {
            throw new MemberException("FORBIDDEN", "Bạn không có quyền thực hiện thao tác này.");
        }
    }

    private void validateSlotAndAssign(DonationRegistrationDTO dto, User user) {
        if (dto.getSlotId() == null) {
            throw new MemberException("SLOT_REQUIRED", "Bạn cần chọn khung giờ hiến máu.");
        }
        slotService.validateSlotAvailable(dto.getSlotId());
    }

    private void sendConfirmationEmail(DonationRegistration reg) {
        String content = String.format("""
                <h3>Xin chào %s</h3>
                <p>Đơn đăng ký hiến máu của bạn vào ngày <b>%s</b> đã được xác nhận.</p>
                <p>Trân trọng cảm ơn!</p>
                """, reg.getUser().getUsername(), reg.getReadyDate());

        eventPublisher.publishEvent(new EmailNotificationEvent(
                this,
                reg.getUser(),
                MessageConstants.DONATION_CONFIRMED,
                content,
                "XÁC NHẬN HIẾN MÁU"
        ));
    }


    private ResponseEntity<?> buildSuccess(Object data, String message) {
        return ResponseEntity.ok(new ApiResponseDTO<>(true, message, data));
    }
}
