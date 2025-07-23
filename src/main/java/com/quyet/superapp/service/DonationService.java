package com.quyet.superapp.service;

<<<<<<< HEAD
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.DonationHistory;
import com.quyet.superapp.mapper.DonationHistoryMapper;
import com.quyet.superapp.repository.DonationHistoryRepository;
import com.quyet.superapp.repository.DonationRepository;
=======
import com.quyet.superapp.dto.DonationHistoryDTO;
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.RecoveryRule;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.repository.DonationRepository;
import com.quyet.superapp.repository.RecoveryRuleRepository;
>>>>>>> origin/main
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
<<<<<<< HEAD
import java.time.LocalDateTime;
=======
>>>>>>> origin/main
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository repository;
<<<<<<< HEAD
    private final DonationHistoryRepository donationHistoryRepository;
    private final DonationHistoryMapper donationHistoryMapper;
=======
    private final RecoveryRuleRepository recoveryRuleRepository;
>>>>>>> origin/main


    public List<Donation> getAll() {
        return repository.findAll();
    }

    public Optional<Donation> getById(Long id) {
        return repository.findById(id);
    }

    public Donation save(Donation donation) {
        return repository.save(donation);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

<<<<<<< HEAD
    /**
     * Tính số ngày còn lại để phục hồi thành phần máu cụ thể
     */
    public int getDaysUntilRecover(Long userId, Long componentId) {
        List<Donation> donations = getByUserId(userId);

        // Tìm lần hiến cuối cùng cho thành phần máu cụ thể
        Optional<LocalDateTime> lastDonation = donations.stream()
                .filter(d -> d.getBloodComponent() != null
                        && d.getBloodComponent().getBloodComponentId().equals(componentId))
                .map(Donation::getDonationDate)
                .filter(date -> date != null)
                .max(LocalDateTime::compareTo);

        if (lastDonation.isEmpty()) {
            return 0; // Nếu chưa từng hiến, có thể hiến ngay
        }

        // Giả sử phục hồi là 30 ngày (sau này có thể cấu hình động)
        LocalDateTime nextEligible = lastDonation.get().plusDays(30);
        int daysLeft = (int) java.time.Duration.between(LocalDateTime.now(), nextEligible).toDays();

        return Math.max(daysLeft, 0); // Không âm
    }



    // ✅ Kiểm tra người dùng đã đủ thời gian để hiến lại thành phần máu cụ thể hay chưa
    public boolean canDonateNow(Long userId, Long componentId) {
        List<Donation> donations = getByUserId(userId).stream()
                .filter(d -> d.getBloodComponent() != null
                        && d.getBloodComponent().getBloodComponentId().equals(componentId))
                .filter(d -> d.getStatus() != null && d.getStatus().name().equals("DONATED"))
                .toList();

        return donations.stream()
                .map(Donation::getDonationDate)
                .filter(date -> date != null)
                .max(LocalDateTime::compareTo)
                .map(lastDonationDate -> lastDonationDate.plusDays(30).isBefore(LocalDateTime.now()))
                .orElse(true);
    }



    // ✅ Lấy tất cả đơn hiến máu theo userId
    public List<Donation> getByUserId(Long userId) {
        return repository.findByUser_UserId(userId); // Viết thêm trong DonationRepository
    }



    // ✅ Đếm số lượt hiến máu theo ngày
    public long countByDate(LocalDate date) {
        return repository.countByCreatedAtBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );
    }


    // ✅ Lấy các đơn hiến máu chưa được phân tách (chưa có log)
    public List<Donation> getUnseparatedDonations() {
        return repository.findAll().stream()
                .filter(d -> d.getDonationDate() != null && d.getVolumeMl() != null) // đơn có dữ liệu
                .filter(d -> d.getBloodType() != null && d.getBloodComponent() != null) // có nhóm máu và thành phần
                .filter(d -> d.getDonationId() != null) // cần để mapping
                .filter(d -> d.getRegistration() != null) // đã qua đăng ký
                .filter(d -> d.getBloodComponent().getCode() != null) // có code thì mới phân tách
                .collect(Collectors.toList());
    }
    public void saveDonationHistory(Donation donation,
                                    boolean paymentCompleted,
                                    int paymentAmount,
                                    String transactionCode,
                                    String paymentMethod) {

        DonationHistory history = DonationHistory.builder()
                .donor(donation.getUser())
                .donation(donation)
                .donatedAt(Optional.ofNullable(donation.getDonationDate()).orElse(LocalDateTime.now()))
                .bloodType(Optional.ofNullable(donation.getBloodType())
                        .map(bt -> bt.getDescription())
                        .orElse("Không rõ"))
                .componentDonated(Optional.ofNullable(donation.getBloodComponent())
                        .map(bc -> bc.getName())
                        .orElse("Không rõ"))
                .volumeMl(donation.getVolumeMl())
                .donationLocation(donation.getLocation())
                .paymentCompleted(paymentCompleted)
                .paymentAmount(paymentAmount)
                .transactionCode(transactionCode)
                .paymentMethod(paymentMethod)
                .createdAt(LocalDateTime.now())
                .build();

        donationHistoryRepository.save(history);
    }


}
=======
    // ✅ Lấy tất cả đơn hiến máu theo userId
    public List<Donation> getByUserId(Long userId) {
        return repository.findByUser_UserId(userId);
    }

    // ✅ Đếm số lượt hiến máu theo ngày (LocalDate chuẩn hóa)
    public long countByDate(LocalDate date) {
        return repository.countByCollectedAt(date);
    }

    // ✅ Lấy các đơn hiến máu chưa được phân tách (chưa có BloodUnit)
    public List<Donation> getUnseparatedDonations() {
        return repository.findByBloodUnitsIsEmpty();
    }

    public List<DonationHistoryDTO> getHistoryByUserId(Long userId) {
        LocalDate today = LocalDate.now();

        return repository.findByUser_UserId(userId).stream()
                .map(d -> {
                    // Lấy loại thành phần máu
                    BloodComponentType componentType = d.getComponent() != null && d.getComponent().getType() != null
                            ? d.getComponent().getType()
                            : BloodComponentType.RBC; // fallback nếu chưa rõ → toàn phần

                    // Lấy số ngày phục hồi
                    int recoveryDays = recoveryRuleRepository.findByComponentType(componentType)
                            .map(RecoveryRule::getRecoveryDays)
                            .orElse(60); // fallback mặc định

                    LocalDate recoveryDate = d.getCollectedAt().plusDays(recoveryDays);
                    boolean isRecovered = recoveryDate.isBefore(today);

                    return DonationHistoryDTO.builder()
                            .donationDate(d.getCollectedAt())
                            .location(d.getLocation())
                            .volumeMl(d.getVolumeMl())
                            .bloodGroup(d.getBloodType() != null ? d.getBloodType().getDescription() : "Chưa rõ")
                            .component(d.getComponent() != null ? d.getComponent().getName() : "Chưa tách")
                            .status(d.getStatus() != null ? d.getStatus().name() : "Không rõ")
                            .recoveryDate(recoveryDate)
                            .isRecovered(isRecovered)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // ✅ Lấy các lượt hiến máu theo khoảng ngày
    public List<Donation> getDonationsBetween(LocalDate start, LocalDate end) {
        return repository.findByCollectedAtBetween(start, end);
    }
}
>>>>>>> origin/main
