package com.quyet.superapp.service;

import com.quyet.superapp.dto.DonationHistoryDTO;
import com.quyet.superapp.entity.Donation;
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
                .filter(d -> d.getBloodType() != null && d.getComponent() != null) // có nhóm máu và thành phần
                .filter(d -> d.getDonationId() != null) // cần để mapping
                .filter(d -> d.getRegistration() != null) // đã qua đăng ký
                .filter(d -> d.getComponent().getCode() != null) // có code thì mới phân tách
                .collect(Collectors.toList());
    }

    public List<DonationHistoryDTO> getHistoryByUserId(Long userId) {
        return repository.findByUser_UserId(userId).stream()
                .map(d -> DonationHistoryDTO.builder()
                        .donationDate(d.getDonationDate())
                        .location(d.getLocation())
                        .volumeMl(d.getVolumeMl())
                        .bloodGroup(d.getBloodType() != null ? d.getBloodType().getDescription() : "Chưa rõ")
                        .component(d.getComponent() != null ? d.getComponent().getName() : "Chưa tách")
                        .status(d.getStatus() != null ? d.getStatus().name() : "Không rõ")
                        .build())
                .collect(Collectors.toList());
    }
}