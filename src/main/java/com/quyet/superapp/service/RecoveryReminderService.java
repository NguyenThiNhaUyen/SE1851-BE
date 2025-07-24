package com.quyet.superapp.service;

import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.enums.DonorReadinessLevel;
import com.quyet.superapp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final UserProfileRepository userProfileRepository;
    private final EmailService emailService;

    @Value("${recovery.reminder.lookback-days:120}")
    private int lookbackDays;

    @Value("${recovery.reminder.min-days-before-remind:7}")
    private int minDaysBeforeRemind;

    @Value("${recovery.reminder.interval-days:30}")
    private int remindIntervalDays;

    @Value("${recovery.reminder.pre-remind-days:3}")
    private int preRemindDays;

    private static final int DEFAULT_RECOVERY_DAYS = 60;

    private static final Map<BloodComponentType, Integer> DEFAULT_RECOVERY_MAP = Map.of(
            BloodComponentType.RBC, 84,
            BloodComponentType.PLASMA, 14,
            BloodComponentType.PLATELET, 28,
            BloodComponentType.WBC, 35
    );

    @Scheduled(cron = "${recovery.reminder.cron}")
    @Transactional
    public void checkAndSendRecoveryReminders() {
        LocalDate today = LocalDate.now();
        LocalDate fromDate = today.minusDays(lookbackDays);
        LocalDate toDate = today.minusDays(minDaysBeforeRemind);

        List<Donation> donations = donationRepository.findByCollectedAtBetweenAndStatus(
                fromDate, toDate, DonationStatus.DONATED
        );

        log.info("🔍 Kiểm tra {} lượt hiến máu từ {} đến {}", donations.size(), fromDate, toDate);

        for (Donation donation : donations) {
            handleRecoveryReminder(donation, false);
        }
    }

    public void scheduleRecoveryReminder(Donation donation) {
        handleRecoveryReminder(donation, true);
    }

    private void handleRecoveryReminder(Donation donation, boolean immediateSendIfReady) {
        if (donation == null || donation.getCollectedAt() == null) return;

        User user = donation.getUser();
        BloodComponentType componentType = inferBloodComponentFromDonation(donation);
        if (componentType == null) return;

        int recoveryDays = recoveryRuleRepository.findByComponentType(componentType)
                .map(RecoveryRule::getRecoveryDays)
                .orElse(DEFAULT_RECOVERY_MAP.getOrDefault(componentType, DEFAULT_RECOVERY_DAYS));

        LocalDate collectedAt = donation.getCollectedAt();
        LocalDate nextEligibleDate = collectedAt.plusDays(recoveryDays);
        LocalDate today = LocalDate.now();

        userProfileRepository.findByUser(user).ifPresent(profile -> {
            if (profile.getRecoveryTime() == null || profile.getRecoveryTime().isBefore(nextEligibleDate)) {
                profile.setRecoveryTime(nextEligibleDate);
                userProfileRepository.save(profile);
            }
        });

        if (immediateSendIfReady && today.isEqual(nextEligibleDate)) {
            sendReminder(user, componentType, nextEligibleDate, "Gửi ngay sau khi hiến máu");
            return;
        }

        if (today.isBefore(nextEligibleDate.minusDays(preRemindDays))) return;

        boolean remindedRecently = reminderLogRepository.existsByUserAndReminderSentAtBetween(
                user,
                LocalDateTime.now().minusDays(remindIntervalDays),
                LocalDateTime.now()
        );
        if (remindedRecently) return;

        if (today.isBefore(nextEligibleDate)) {
            sendPreReminder(user, componentType, nextEligibleDate);
        } else {
            sendReminder(user, componentType, nextEligibleDate, "Nhắc đã đủ điều kiện phục hồi");
        }
    }

    private void sendReminder(User user, BloodComponentType componentType, LocalDate nextEligibleDate, String note) {
        emailService.sendRecoveryReminder(user, componentType, nextEligibleDate);
        saveLog(user, componentType, nextEligibleDate, note);
        log.info("📧 Gửi recovery reminder cho {} (nextEligible={})", user.getUsername(), nextEligibleDate);
    }

    private void sendPreReminder(User user, BloodComponentType componentType, LocalDate nextEligibleDate) {
        emailService.sendPreRecoveryReminder(user, componentType, nextEligibleDate);
        saveLog(user, componentType, nextEligibleDate, "Nhắc sắp phục hồi");
        log.info("📧 Gửi pre-reminder cho {} (nextEligible={})", user.getUsername(), nextEligibleDate);
    }

    private void saveLog(User user, BloodComponentType type, LocalDate nextDate, String note) {
        reminderLogRepository.save(
                ReminderLog.builder()
                        .user(user)
                        .bloodComponent(type.name())
                        .nextEligibleDate(nextDate.atStartOfDay())
                        .reminderSentAt(LocalDateTime.now())
                        .note(note)
                        .build()
        );
    }

    private BloodComponentType inferBloodComponentFromDonation(Donation donation) {
        if (donation.getComponent() != null && donation.getComponent().getType() != null) {
            return donation.getComponent().getType();
        }

        DonorReadinessLevel level = donation.getRegistration() != null
                ? donation.getRegistration().getReadinessLevel()
                : null;

        if (level == DonorReadinessLevel.EMERGENCY_NOW || level == DonorReadinessLevel.EMERGENCY_FLEXIBLE) {
            return BloodComponentType.PLASMA;
        }

        return BloodComponentType.RBC;
    }
}
