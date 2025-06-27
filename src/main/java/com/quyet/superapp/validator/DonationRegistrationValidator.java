package com.quyet.superapp.validator;

import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.repository.DonationRegistrationRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DonationRegistrationValidator {
    private final DonationRegistrationRepository registrationRepository;

    private static final int MIN_HOURS_BEFORE_SCHEDULE = 2;
    private static final int MIN_RECOVERY_DAYS = 90;

    public void validateRegistrationRequest(User user, DonationRegistrationDTO dto) {

        // ❌ Kiểm tra thời gian hẹn tối thiểu trước X giờ
        if (dto.getScheduledDate().isBefore(LocalDate.now().plusDays(MIN_HOURS_BEFORE_SCHEDULE))) {
            throw new MemberException("SCHEDULE_TOO_SOON",
                    "Vui lòng chọn thời gian hẹn cách hiện tại tối thiểu " + MIN_HOURS_BEFORE_SCHEDULE + " giờ.");
        }

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
    }
}
