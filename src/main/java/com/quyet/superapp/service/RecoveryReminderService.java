package com.quyet.superapp.service;

import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.enums.DonorReadinessLevel;
import com.quyet.superapp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecoveryReminderService {

    private final DonationRepository donationRepository;
    private final RecoveryRuleRepository recoveryRuleRepository;
    private final ReminderLogRepository reminderLogRepository;
    private final EmailService emailService;

    private static final int LOOKBACK_DAYS = 120;
    private static final int MIN_DAYS_BEFORE_REMIND = 7;
    private static final int REMIND_INTERVAL_DAYS = 30;
    private static final int DEFAULT_RECOVERY_DAYS = 60;

    private static final Map<BloodComponentType, Integer> DEFAULT_RECOVERY_MAP = Map.of(
            BloodComponentType.RBC, 84,       // ≥ 12 tuần
            BloodComponentType.PLASMA, 14,    // ≥ 2 tuần
            BloodComponentType.PLATELET, 28,  // ≥ 4 tuần
            BloodComponentType.WBC, 35        // ≈ 3–6 tuần → chọn trung bình
    );
    @Scheduled(cron = "${recovery.reminder.cron}")// Chạy mỗi ngày lúc 9:00 AM
    @Transactional
    public void checkAndSendRecoveryReminders() {
        LocalDate today = LocalDate.now();
        LocalDate fromDate = today.minusDays(LOOKBACK_DAYS);
        LocalDate toDate = today.minusDays(MIN_DAYS_BEFORE_REMIND);

        List<Donation> donations = donationRepository.findByCollectedAtBetweenAndStatus(
                fromDate, toDate, DonationStatus.DONATED
        );

        log.info("🔍 Tổng số lượt hiến máu từ {} đến {}: {}", fromDate, toDate, donations.size());

        for (Donation donation : donations) {
            BloodComponentType componentType = inferBloodComponentFromDonation(donation);
            if (componentType == null) {
                log.warn("⚠️ Không xác định được thành phần máu từ donationId={}", donation.getDonationId());
                continue;
            }

            int recoveryDays = recoveryRuleRepository.findByComponentType(componentType)
                    .map(RecoveryRule::getRecoveryDays)
                    .orElse(DEFAULT_RECOVERY_MAP.getOrDefault(componentType, DEFAULT_RECOVERY_DAYS));

            LocalDate nextEligibleDate = donation.getCollectedAt().plusDays(recoveryDays);

            if (!nextEligibleDate.isBefore(today)) {
                log.info("⏳ Chưa đến ngày phục hồi của user {} (nextEligibleDate={})", donation.getUser().getUsername(), nextEligibleDate);
                continue;
            }

            boolean alreadyReminded = reminderLogRepository.existsByUserAndReminderSentAtBetween(
                    donation.getUser(),
                    LocalDateTime.now().minusDays(REMIND_INTERVAL_DAYS),
                    LocalDateTime.now()
            );

            if (alreadyReminded) {
                log.info("🛑 Đã nhắc user {} trong vòng {} ngày", donation.getUser().getUsername(), REMIND_INTERVAL_DAYS);
                continue;
            }

            // ✅ Gửi email nhắc nhở
            emailService.sendRecoveryReminder(donation.getUser(), componentType, nextEligibleDate);
            log.info("📧 Gửi email nhắc phục hồi cho user={} (component={}, nextEligible={})",
                    donation.getUser().getUsername(), componentType.name(), nextEligibleDate);

            // ✅ Ghi log lại
            ReminderLog logEntry = ReminderLog.builder()
                    .user(donation.getUser())
                    .bloodComponent(componentType.name())
                    .nextEligibleDate(nextEligibleDate.atStartOfDay())
                    .reminderSentAt(LocalDateTime.now())
                    .note("Nhắc phục hồi tự động")
                    .build();

            reminderLogRepository.save(logEntry);
        }
    }

    private BloodComponentType inferBloodComponentFromDonation(Donation donation) {
        if (donation.getComponent() != null && donation.getComponent().getType() != null) {
            return donation.getComponent().getType();
        }

        // Nếu component chưa có, kiểm tra readiness level
        DonorReadinessLevel readinessLevel = donation.getRegistration() != null
                ? donation.getRegistration().getReadinessLevel()
                : null;

        if (readinessLevel == DonorReadinessLevel.EMERGENCY_NOW || readinessLevel == DonorReadinessLevel.EMERGENCY_FLEXIBLE) {
            return BloodComponentType.PLASMA; // mặc định thành phần thường dùng trong truyền khẩn cấp
        }

        return BloodComponentType.RBC; // mặc định là toàn phần
    }

}
