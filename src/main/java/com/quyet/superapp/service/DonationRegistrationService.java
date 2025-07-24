package com.quyet.superapp.service;

import com.quyet.superapp.config.jwt.UserPrincipal;
import com.quyet.superapp.constant.MessageConstants;
import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.entity.address.Address;

import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.enums.HealthCheckFailureReason;

import com.quyet.superapp.enums.SlotStatus;
import com.quyet.superapp.event.EmailNotificationEvent;
import com.quyet.superapp.exception.MemberException;

import com.quyet.superapp.mapper.DonationRegistrationMapper;
import com.quyet.superapp.repository.*;
import com.quyet.superapp.repository.address.AddressRepository;

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
@Slf4j
public class DonationRegistrationService {

    private final DonationRegistrationRepository registrationRepo;
    private final UserRepository userRepo;
    private final UserProfileRepository profileRepo;
    private final AddressRepository addressRepo;
    private final DonationRepository donationRepo;
    private final HealthCheckFailureLogService failureLogService;
    private final ApplicationEventPublisher eventPublisher;
    private final DonationRegistrationValidator validator;
    private final DonationSlotService slotService;
    private final BloodBagService bloodBagService;
    private final DonationSlotRepository  donationSlotRepository;

    // ✅ Đăng ký hiến máu
    public ResponseEntity<?> register(Long userId, DonationRegistrationDTO dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new MemberException("USER_NOT_FOUND", MessageConstants.USER_NOT_FOUND));

        validator.validateRegistrationRequest(user, dto);
        updateOrCreateUserProfile(user, dto);

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

        DonationRegistration reg = DonationRegistrationMapper.toEntity(dto, user);
        reg.setSlot(assignedSlot);
        reg.setStatus(DonationStatus.PENDING);
        reg.setReadyDate(assignedSlot.getSlotDate());
        reg.setLocation(assignedSlot.getLocation());

        registrationRepo.save(reg);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.DONATION_REGISTERED, DonationRegistrationMapper.toDTO(reg)));
    }


    // ✅ Xác nhận đơn đăng ký
    public ResponseEntity<?> confirm(Long regId, UserPrincipal principal) {
        User staff = getUserById(principal.getUserId());
        validateStaffOrAdmin(staff);

        DonationRegistration reg = getRegistrationOrThrow(regId);
        validateStatus(reg, DonationStatus.PENDING);

        reg.setStatus(DonationStatus.CONFIRMED);
        reg.setConfirmedBy(staff);
        registrationRepo.save(reg);

        sendConfirmationEmail(reg);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.DONATION_CONFIRMED, DonationRegistrationMapper.toDTO(reg)));
    }

    // ✅ Hủy đơn
    public ResponseEntity<?> markAsCancelled(Long regId) {
        DonationRegistration reg = getRegistrationOrThrow(regId);
        validateStatus(reg, DonationStatus.CONFIRMED);

        reg.setStatus(DonationStatus.CANCELLED);
        registrationRepo.save(reg);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.DONATION_CANCELLED, DonationRegistrationMapper.toDTO(reg)));
    }

    // ✅ Đánh dấu không đủ sức khỏe
    public ResponseEntity<?> markAsFailedHealth(Long regId, HealthCheckFailureReason reason, String staffNote) {
        DonationRegistration reg = getRegistrationOrThrow(regId);
        validateStatus(reg, DonationStatus.CONFIRMED);

        reg.setStatus(DonationStatus.FAILED_HEALTH);
        registrationRepo.save(reg);
        failureLogService.saveLog(regId, reason, staffNote);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.DONATION_FAILED_HEALTH, DonationRegistrationMapper.toDTO(reg)));
    }

    // ✅ Đánh dấu đã hiến máu và tạo túi máu nếu chưa có
    public ResponseEntity<?> markAsDonated(Long regId) {
        DonationRegistration reg = getRegistrationOrThrow(regId);
        Donation donation = donationRepo.findByRegistration_RegistrationId(regId)
                .orElseThrow(() -> new MemberException("NOT_YET_DONATED", "Chưa có bản ghi hiến máu tương ứng"));

        // Nếu chưa có túi máu → tạo mới
        if (donation.getBloodBag() == null) {
            BloodBag bag = bloodBagService.createFromDonation(donation);
            donation.setBloodBag(bag);
            donationRepo.save(donation);
        }

        // Cập nhật trạng thái đơn đăng ký
        reg.setStatus(DonationStatus.DONATED);
        registrationRepo.save(reg);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "✅ Cập nhật trạng thái DONATED & tạo túi máu thành công", DonationRegistrationMapper.toDTO(reg)));
    }

    // ✅ Lấy danh sách theo trạng thái
    public List<DonationRegistrationDTO> getByStatus(DonationStatus status) {
        return registrationRepo.findByStatus(status)
                .stream().map(DonationRegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }
    // ✅ Lấy toàn bộ
    public ResponseEntity<?> getAllDTO() {
        List<DonationRegistrationDTO> list = registrationRepo.findAll()
                .stream().map(DonationRegistrationMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.GET_REGISTRATION_SUCCESS, list));
    }

    public ResponseEntity<?> getDTOById(Long id) {
        DonationRegistration reg = getRegistrationOrThrow(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.GET_REGISTRATION_SUCCESS, DonationRegistrationMapper.toDTO(reg)));
    }

    public List<DonationRegistrationDTO> getBySlotId(Long slotId) {
        return registrationRepo.findBySlot_SlotId(slotId)
                .stream().map(DonationRegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ===================== Helper Methods =====================

    private User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new MemberException("USER_NOT_FOUND", MessageConstants.USER_NOT_FOUND));
    }

    private DonationRegistration getRegistrationOrThrow(Long id) {
        return registrationRepo.findById(id)
                .orElseThrow(() -> new MemberException("REGISTRATION_NOT_FOUND", MessageConstants.DONATION_REGISTRATION_NOT_FOUND));
    }

    private void validateStatus(DonationRegistration reg, DonationStatus expected) {
        if (reg.getStatus() != expected) {
            throw new MemberException("INVALID_STATUS", MessageConstants.INVALID_REGISTRATION_STATUS);
        }
    }

    private void validateStaffOrAdmin(User user) {
        String role = user.getRole().getName();
        if (!"STAFF".equals(role) && !"ADMIN".equals(role)) {
            throw new MemberException("FORBIDDEN", "Bạn không có quyền xác nhận đơn đăng ký");
        }
    }

    private void sendConfirmationEmail(DonationRegistration reg) {
        String content = String.format(
                "<h3>Xin chào %s</h3><p>Đơn đăng ký hiến máu của bạn vào ngày <b>%s</b> đã được xác nhận.</p><p>Trân trọng cảm ơn!</p>",
                reg.getUser().getUsername(), reg.getReadyDate());

        eventPublisher.publishEvent(new EmailNotificationEvent(this, reg.getUser(), MessageConstants.DONATION_CONFIRMED, content, "XÁC NHẬN HIẾN MÁU"));
    }

    private void updateOrCreateUserProfile(User user, DonationRegistrationDTO dto) {
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
        }

        profile.setFullName(dto.getFullName());
        profile.setDob(dto.getDob());
        profile.setGender(dto.getGender());
        profile.setPhone(dto.getPhone());
        profile.setBloodType(dto.getBloodType());

        if (dto.getAddressId() != null) {
            Address address = addressRepo.findById(dto.getAddressId())
                    .orElseThrow(() -> new MemberException("ADDRESS_NOT_FOUND", MessageConstants.ADDRESS_NOT_FOUND));
            profile.setAddress(address);
        }

        profileRepo.save(profile);
    }
}
