package com.quyet.superapp.repository;

import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.enums.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    // Đếm số người hiến máu vào ngày nhất định
    long countByCollectedAt(LocalDate date);
    Optional<Donation> findByRegistration_RegistrationId(Long regId);
    List<Donation> findByUser_UserId(Long userId);
    List<Donation> findByBloodUnitsIsEmpty();
    List<Donation> findByStatus(DonationStatus status);
    // Lấy danh sách hiến máu theo ngày
    List<Donation> findByCollectedAt(LocalDate date);

    // Lọc Donation theo khoảng ngày
    List<Donation> findByCollectedAtBetween(LocalDate start, LocalDate end);

    List<Donation> findByCollectedAtBetweenAndStatus(LocalDate from, LocalDate to, DonationStatus status);




}