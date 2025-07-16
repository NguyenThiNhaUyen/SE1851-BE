package com.quyet.superapp.validator;

import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.repository.DonationRegistrationRepository;
import com.quyet.superapp.service.DonationSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DonationRegistrationValidator {

    private final DonationRegistrationRepository registrationRepository;
    private final DonationSlotService donationSlotService;

    private static final int MIN_RECOVERY_DAYS = 90;

    public void validateRegistrationRequest(User user, DonationRegistrationDTO dto) {

        // ❌ Đã có đơn đăng ký PENDING chưa xử lý
        if (registrationRepository.existsByUser_UserIdAndStatus(user.getUserId(), DonationStatus.PENDING)) {
            throw new MemberException("DUPLICATE_PENDING",
                    "Bạn đã có một đơn đăng ký đang chờ xác nhận.");
        }

        // ❌ Thời gian phục hồi chưa đủ (nếu có)
        UserProfile profile = user.getUserProfile();
        if (profile != null && profile.getLastDonationDate() != null) {
            LocalDate nextEligible = profile.getLastDonationDate().plusDays(MIN_RECOVERY_DAYS);
            if (dto.getScheduledDate().isBefore(nextEligible)) {
                throw new MemberException("RECOVERY_NOT_FINISHED",
                        "Bạn cần đợi đến sau " + nextEligible + " mới có thể đăng ký hiến máu.");
            }
        }

        // ❌ Slot không được null và phải còn chỗ
        if (dto.getSlotId() == null) {
            throw new MemberException("SLOT_REQUIRED", "Bạn cần chọn khung giờ hiến máu.");
        }

        if (!donationSlotService.isSlotAvailable(dto.getSlotId())) {
            throw new MemberException("SLOT_FULL", "Khung giờ bạn chọn đã đầy, vui lòng chọn khung giờ khác.");
        }
    }
}
