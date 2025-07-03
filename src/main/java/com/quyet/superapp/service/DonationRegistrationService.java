package com.quyet.superapp.service;

import com.quyet.superapp.config.jwt.UserPrincipal;
import com.quyet.superapp.constant.MessageConstants;
import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.event.EmailNotificationEvent;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.mapper.DonationRegistrationMapper;
import com.quyet.superapp.repository.*;
import com.quyet.superapp.repository.address.AddressRepository;
import com.quyet.superapp.validator.DonationRegistrationValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonationRegistrationService {

    private final DonationRegistrationRepository donationRegistrationRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AddressRepository addressRepository;
    private final DonationRepository donationRepository;
    private final BloodTypeRepository bloodTypeRepository;
    private final HealthCheckFormRepository healthCheckFormRepository;
    private final HealthCheckFailureLogService healthCheckFailureLogService;
    private final ApplicationEventPublisher eventPublisher;
    private final DonationRegistrationValidator donationRegistrationValidator;
    private final DonationSlotService donationSlotService;

    public ResponseEntity<?> register(Long userId, DonationRegistrationDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MemberException("USER_NOT_FOUND", MessageConstants.USER_NOT_FOUND));

        donationRegistrationValidator.validateRegistrationRequest(user, dto);

        // Nếu chưa có hồ sơ → tạo mới
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
            profile.setFullName(dto.getFullName());
            profile.setDob(dto.getDob());
            profile.setGender(dto.getGender());
            profile.setBloodType(dto.getBloodType());

            if (dto.getAddressId() != null) {
                Address address = addressRepository.findById(dto.getAddressId())
                        .orElseThrow(() -> new MemberException("ADDRESS_NOT_FOUND", MessageConstants.ADDRESS_NOT_FOUND));
                profile.setAddress(address);
            }
            profile.setPhone(dto.getPhone());
            userProfileRepository.save(profile);
        }

        // Tạo đăng ký
        DonationRegistration registration = DonationRegistrationMapper.toEntity(dto, user);
        registration.setStatus(DonationStatus.PENDING);

        // Gán slot
        if (dto.getSlotId() != null) {
            // Nếu slot đã đầy → tự động gán slot khác gần nhất
            if (!donationSlotService.isSlotAvailable(dto.getSlotId())) {
                log.warn("Slot {} đã đầy, hệ thống sẽ gợi ý slot khác", dto.getSlotId());
                donationSlotService.autoAssignSlotToRegistration(registration);
            } else {
                donationSlotService.assignSlotToRegistration(registration, dto.getSlotId());
            }
        } else {
            donationSlotService.autoAssignSlotToRegistration(registration);
        }

        donationRegistrationRepository.save(registration);

        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                MessageConstants.DONATION_REGISTERED,
                DonationRegistrationMapper.toDTO(registration)
        ));
    }


    public ResponseEntity<?> confirm(Long registrationId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Long staffId = principal.getUserId();

        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new MemberException("USER_NOT_FOUND", MessageConstants.USER_NOT_FOUND));

        String role = staff.getRole().getName();
        if (!"STAFF".equals(role) && !"ADMIN".equals(role)) {
            throw new MemberException("FORBIDDEN", "Bạn không có quyền xác nhận đơn đăng ký");
        }

        DonationRegistration reg = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("REGISTRATION_NOT_FOUND", MessageConstants.DONATION_REGISTRATION_NOT_FOUND));

        if (reg.getStatus() != DonationStatus.PENDING) {
            throw new MemberException("INVALID_STATUS", MessageConstants.INVALID_REGISTRATION_STATUS);
        }

        reg.setStatus(DonationStatus.CONFIRMED);
        reg.setConfirmedBy(staff);
        donationRegistrationRepository.save(reg);

        eventPublisher.publishEvent(new EmailNotificationEvent(this,
                reg.getUser(),
                MessageConstants.DONATION_CONFIRMED,
                String.format("<h3>Chào %s</h3><p>Đơn đăng ký hiến máu của bạn vào ngày <b>%s</b> đã được xác nhận bởi nhân viên.</p><p>Cảm ơn bạn vì nghĩa cử cao đẹp!</p>",
                        reg.getUser().getUsername(), reg.getReadyDate()),
                "XÁC NHẬN"));

        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.DONATION_CONFIRMED, DonationRegistrationMapper.toDTO(reg)));
    }

    public ResponseEntity<?> createDonationIfEligible(Long registrationId) {
        DonationRegistration reg = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("NOT_FOUND", MessageConstants.DONATION_REGISTRATION_NOT_FOUND));

        if (reg.getStatus() != DonationStatus.CONFIRMED) {
            throw new MemberException("INVALID_STATUS", MessageConstants.INVALID_REGISTRATION_STATUS);
        }

        HealthCheckForm form = healthCheckFormRepository.findByRegistration_RegistrationId(registrationId);
        if (form == null || !Boolean.TRUE.equals(form.getIsEligible())) {
            throw new MemberException("NOT_ELIGIBLE", MessageConstants.HEALTH_NOT_ELIGIBLE);
        }

        if (donationRepository.existsByRegistration_RegistrationId(registrationId)) {
            throw new MemberException("DUPLICATE_DONATION", MessageConstants.DONATION_ALREADY_EXISTS);
        }

        BloodType bloodType = bloodTypeRepository.findByDescription(reg.getBloodType().getDescription())
                .orElseThrow(() -> new MemberException("NOT_FOUND", MessageConstants.BLOOD_TYPE_NOT_FOUND));

        Donation donation = new Donation();
        donation.setUser(reg.getUser());
        donation.setRegistration(reg);
        donation.setVolumeMl(450);
        donation.setBloodType(bloodType);
        donation.setDonationDate(reg.getReadyDate());

        donationRepository.save(donation);

        UserProfile profile = reg.getUser().getUserProfile();
        profile.setLastDonationDate(reg.getReadyDate());
        profile.setRecoveryTime(90);
        userProfileRepository.save(profile);

        reg.setStatus(DonationStatus.DONATED);
        donationRegistrationRepository.save(reg);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.DONATION_CREATED, donation));
    }

    public ResponseEntity<?> getDTOById(Long id) {
        DonationRegistration registration = donationRegistrationRepository.findById(id)
                .orElseThrow(() -> new MemberException("NOT_FOUND", MessageConstants.DONATION_REGISTRATION_NOT_FOUND));
        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.GET_REGISTRATION_SUCCESS, DonationRegistrationMapper.toDTO(registration)));
    }

    public ResponseEntity<?> getAllDTO() {
        List<DonationRegistrationDTO> registrations = donationRegistrationRepository.findAll().stream()
                .map(DonationRegistrationMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.GET_REGISTRATION_SUCCESS, registrations));
    }

    public List<DonationRegistrationDTO> getByStatus(DonationStatus status) {
        return donationRegistrationRepository.findByStatus(status).stream()
                .map(DonationRegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> markAsCancelled(Long registrationId) {
        DonationRegistration reg = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("REGISTRATION_NOT_FOUND", MessageConstants.DONATION_REGISTRATION_NOT_FOUND));

        if (reg.getStatus() != DonationStatus.CONFIRMED) {
            throw new MemberException("INVALID_STATUS", MessageConstants.INVALID_REGISTRATION_STATUS);
        }

        reg.setStatus(DonationStatus.CANCELLED);
        donationRegistrationRepository.save(reg);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.DONATION_CANCELLED, DonationRegistrationMapper.toDTO(reg)));
    }

    public ResponseEntity<?> markAsFailedHealth(Long registrationId, String reason, String staffNote) {
        DonationRegistration reg = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("REGISTRATION_NOT_FOUND", MessageConstants.DONATION_REGISTRATION_NOT_FOUND));

        if (reg.getStatus() != DonationStatus.CONFIRMED) {
            throw new MemberException("INVALID_STATUS", MessageConstants.INVALID_REGISTRATION_STATUS);
        }

        reg.setStatus(DonationStatus.FAILED_HEALTH);
        donationRegistrationRepository.save(reg);
        healthCheckFailureLogService.saveLog(registrationId, reason, staffNote);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, MessageConstants.DONATION_FAILED_HEALTH, DonationRegistrationMapper.toDTO(reg)));
    }

    public void assignSlotToRegistration(Long registrationId, Long slotId) {
        DonationRegistration registration = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.DONATION_REGISTRATION_NOT_FOUND));

        donationSlotService.assignSlotToRegistration(registration, slotId);
    }

    public List<DonationRegistrationDTO> getBySlotId(Long slotId) {
        return donationRegistrationRepository.findBySlot_SlotId(slotId).stream()
                .map(DonationRegistrationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> markAsDonated(Long registrationId) {
        DonationRegistration reg = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy đơn đăng ký"));

        if (!donationRepository.existsByRegistration_RegistrationId(registrationId)) {
            throw new MemberException("NOT_YET_DONATED", "Chưa có bản ghi hiến máu tương ứng");
        }

        reg.setStatus(DonationStatus.DONATED);
        donationRegistrationRepository.save(reg);

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Cập nhật trạng thái DONATED thành công", DonationRegistrationMapper.toDTO(reg)));
    }
}
