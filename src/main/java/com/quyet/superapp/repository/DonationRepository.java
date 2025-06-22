package com.quyet.superapp.repository;

import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.enums.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    // Đếm số người hiến máu vào ngày nhất định
    long countByDonationDate(LocalDateTime date);
    boolean existsByRegistration_RegistrationId(Long registrationId);
    List<Donation> findByUser_UserId(Long userId);
    List<Donation> findByBloodUnitsIsEmpty();
    List<Donation> findByStatus(DonationStatus status);

}