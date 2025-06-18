package com.quyet.superapp.repository;

import com.quyet.superapp.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    // Đếm số người hiến máu vào ngày nhất định
    long countByDonationDate(LocalDate date);
}