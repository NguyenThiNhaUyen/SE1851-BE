
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

    private final DonationRepository donationRepository;
    private final RecoveryRuleRepository recoveryRuleRepository;

    /**
     * Lấy toàn bộ bản ghi hiến máu
     */
    public List<Donation> getAll() {
        return donationRepository.findAll();
    }

    /**
     * Tìm lượt hiến máu theo ID
     */
    public Optional<Donation> getById(Long id) {
        return donationRepository.findById(id);
    }

    /**
     * Lưu hoặc cập nhật lượt hiến máu
     */
    public Donation save(Donation donation) {
        return donationRepository.save(donation);
    }

    /**
     * Xóa lượt hiến máu theo ID
     */
    public void deleteById(Long id) {
        donationRepository.deleteById(id);
    }

    /**
     * Lấy danh sách hiến máu theo userId
     */
    public List<Donation> getByUserId(Long userId) {
        return donationRepository.findByUser_UserId(userId);
    }

    /**
     * Đếm số lượt hiến trong ngày
     */
    public long countByDate(LocalDate date) {
        return donationRepository.countByCollectedAt(date);
    }

    /**
     * Lấy danh sách lượt hiến chưa phân tách thành BloodUnit
     */
    public List<Donation> getUnseparatedDonations() {
        return donationRepository.findByBloodUnitsIsEmpty();
    }

    /**
     * Lấy lịch sử hiến máu của user, có tính ngày phục hồi
     */
    public List<DonationHistoryDTO> getHistoryByUserId(Long userId) {
        LocalDate today = LocalDate.now();

        return donationRepository.findByUser_UserId(userId).stream()
                .map(donation -> {
                    BloodComponentType componentType = getComponentType(donation);
                    int recoveryDays = getRecoveryDays(componentType);
                    LocalDate recoveryDate = donation.getCollectedAt().plusDays(recoveryDays);
                    boolean isRecovered = !recoveryDate.isAfter(today); // true nếu <= today

                    return DonationHistoryDTO.builder()
                            .donationDate(donation.getCollectedAt())
                            .location(donation.getLocation())
                            .volumeMl(donation.getVolumeMl())
                            .bloodGroup(donation.getBloodType() != null ? donation.getBloodType().getDescription() : "Chưa rõ")
                            .component(donation.getComponent() != null ? donation.getComponent().getName() : "Chưa tách")
                            .status(donation.getStatus() != null ? donation.getStatus().name() : "Không rõ")
                            .recoveryDate(recoveryDate)
                            .isRecovered(isRecovered)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách lượt hiến theo khoảng thời gian
     */
    public List<Donation> getDonationsBetween(LocalDate start, LocalDate end) {
        return donationRepository.findByCollectedAtBetween(start, end);
    }

    /**
     * Trích xuất loại thành phần máu từ Donation (nếu không có → mặc định RBC)
     */
    private BloodComponentType getComponentType(Donation donation) {
        if (donation.getComponent() != null && donation.getComponent().getType() != null) {
            return donation.getComponent().getType();
        }
        return BloodComponentType.RBC;
    }

    /**
     * Tìm số ngày phục hồi dựa trên loại thành phần máu (nếu không có → 60)
     */
    private int getRecoveryDays(BloodComponentType type) {
        return recoveryRuleRepository.findByComponentType(type)
                .map(RecoveryRule::getRecoveryDays)
                .orElse(60);
    }
}

