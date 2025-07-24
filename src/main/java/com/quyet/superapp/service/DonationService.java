package com.quyet.superapp.service;

import com.quyet.superapp.dto.DonationHistoryDTO;
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.RecoveryRule;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.repository.DonationRepository;
import com.quyet.superapp.repository.RecoveryRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository repository;
    private final RecoveryRuleRepository recoveryRuleRepository;


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
