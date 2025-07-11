package com.quyet.superapp.service;

import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.DonationHistory;
import com.quyet.superapp.mapper.DonationHistoryMapper;
import com.quyet.superapp.repository.DonationHistoryRepository;
import com.quyet.superapp.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository repository;
    private final DonationHistoryRepository donationHistoryRepository;
    private final DonationHistoryMapper donationHistoryMapper;


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
        return repository.findAll().stream()
                .filter(d -> d.getUser() != null && d.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // ✅ Đếm số lượt hiến máu theo ngày
    public long countByDate(LocalDateTime date) {
        return repository.countByDonationDate(date);
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