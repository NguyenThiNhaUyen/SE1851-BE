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

import java.time.LocalDate;

import com.quyet.superapp.config.jwt.UserPrincipal;
import com.quyet.superapp.constant.MessageConstants;
import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.enums.HealthCheckFailureReason;


import com.quyet.superapp.enums.SlotStatus;


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


import java.util.Comparator;


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

    public void createRegularRegistration(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        // Check trùng đăng ký PENDING
        if (donationRegistrationRepository.existsByUser_UserIdAndStatus(userId, DonationStatus.PENDING)) {
            throw new MemberException("DUPLICATE_PENDING", "Bạn đã có đơn đăng ký đang chờ.");
        }

        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new MemberException("MISSING_PROFILE", "Bạn cần cập nhật hồ sơ trước khi đăng ký hiến máu.");
        }

        // Tạo đơn mới
        DonationRegistration reg = new DonationRegistration();
        reg.setUser(user);
        reg.setBloodType(profile.getBloodType().getDescription());
        reg.setStatus(DonationStatus.PENDING);
        reg.setReadyDate(LocalDate.now().plusDays(7).atStartOfDay());

        // Đặt lịch mặc định sau 1 tuần

        donationRegistrationRepository.save(reg);
    }



    // ✅ Đăng ký hiến máu
    public DonationRegistrationDTO register(Long userId, DonationRegistrationDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy User với ID: " + userId));

        // Check PENDING trùng
        if (donationRegistrationRepository.existsByUser_UserIdAndStatus(userId, DonationStatus.PENDING)) {
            throw new MemberException("DUPLICATE_PENDING", "Bạn đã có một đơn đăng ký đang chờ xác nhận.");
        }

        // Tạo UserProfile nếu chưa có
        if (user.getUserProfile() == null) {
            UserProfile profile = new UserProfile();
            profile.setUser(user);
            profile.setFullName(dto.getFullName());
            profile.setDob(dto.getDob());
            profile.setGender(dto.getGender());
            profile.setPhone(dto.getPhone());

            if (dto.getBloodTypeId() != null) {
                BloodType bloodType = bloodTypeRepository.findById(dto.getBloodTypeId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm máu với ID: " + dto.getBloodTypeId()));
                profile.setBloodType(bloodType);
            }

            if (dto.getAddressId() != null) {
                Address address = addressRepository.findById(dto.getAddressId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ với ID: " + dto.getAddressId()));
                profile.setAddress(address);
            }

            userProfileRepository.save(profile);
        }

        // Tạo đơn đăng ký mới
        DonationRegistration registration = DonationRegistrationMapper.toEntity(dto, user);
        registration.setStatus(DonationStatus.PENDING);
        return DonationRegistrationMapper.toDTO(donationRegistrationRepository.save(registration));
    }


    // ✅ Lấy danh sách tất cả đơn đăng ký
    public List<DonationRegistrationDTO> getAllDTO() {
        return donationRegistrationRepository.findAll().stream()
                .map(DonationRegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Lấy đơn đăng ký theo ID
    public DonationRegistrationDTO getDTOById(Long id) {
        return donationRegistrationRepository.findById(id)
                .map(DonationRegistrationMapper::toDTO)
                .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy đơn đăng ký"));
    }

    // ✅ Xác nhận đơn đăng ký (chưa tạo Donation)
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

        return DonationRegistrationMapper.toDTO(reg);
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
                .map(DonationRegistrationMapper::toDTO)
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
        return DonationRegistrationMapper.toDTO(reg);
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

        return DonationRegistrationMapper.toDTO(reg);

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

    private final DonationSlotRepository  donationSlotRepository;

    private final RecoveryReminderService recoveryReminderService;
    private final EmailService emailService;
    private final UserProfileService userProfileService;

    // ✅ Xử lý đăng ký hiến máu từ phía người dùng
    public ResponseEntity<?> register(Long userId, DonationRegistrationDTO dto) {
        User user = findUser(userId);
        validator.validateRegistrationRequest(user, dto);


        // ✅ Nếu slotId không được cung cấp → tìm slot phù hợp theo ngày và location
        DonationSlot assignedSlot;
        if (dto.getSlotId() == null) {
            if (dto.getScheduledDate() == null || dto.getLocation() == null) {
                throw new MemberException("SLOT_REQUIRED", "Bạn cần chọn ngày và địa điểm hiến máu.");
            }

            // Tìm danh sách slot theo ngày và địa điểm
            List<DonationSlot> availableSlots = donationSlotRepository
                    .findBySlotDateAndLocationAndStatus(dto.getScheduledDate(), dto.getLocation(), SlotStatus.ACTIVE)
                    .stream()
                    .filter(slot -> slot.getRegisteredCount() < slot.getMaxCapacity())
                    .sorted(Comparator.comparing(DonationSlot::getStartTime))
                    .collect(Collectors.toList());

            if (availableSlots.isEmpty()) {
                throw new MemberException("NO_AVAILABLE_SLOT", "Không còn slot trống phù hợp vào ngày và địa điểm đã chọn.");
            }

            assignedSlot = availableSlots.get(0); // chọn slot phù hợp sớm nhất
        } else {
            slotService.validateSlotAvailable(dto.getSlotId());
            assignedSlot = slotService.assignSlotToRegistration(new DonationRegistration(), dto.getSlotId());
        }


        userProfileService.updateOrCreateFromRegistration(user, dto);

        validateSlotAndAssign(dto, user);

        DonationRegistration reg = DonationRegistrationMapper.toEntity(dto, user);
        reg.setSlot(assignedSlot);
        reg.setStatus(DonationStatus.PENDING);
        reg.setReadyDate(assignedSlot.getSlotDate());
        reg.setLocation(assignedSlot.getLocation());

        registrationRepo.save(reg);
        emailService.sendPreDonationInstructionEmail(user, dto.getScheduledDate());

        return buildSuccess(DonationRegistrationMapper.toDTO(reg), MessageConstants.DONATION_REGISTERED);
    }



    // ✅ Xác nhận đơn đăng ký

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
