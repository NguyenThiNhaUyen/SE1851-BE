package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodSeparationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodSeparationLogRepository extends JpaRepository<BloodSeparationLog, Long> {
    boolean existsByDonation_DonationId(Long donationId); // dùng để check trùng

}
