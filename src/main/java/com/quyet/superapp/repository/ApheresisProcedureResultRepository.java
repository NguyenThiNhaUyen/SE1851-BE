package com.quyet.superapp.repository;

import com.quyet.superapp.entity.ApheresisProcedureResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApheresisProcedureResultRepository extends JpaRepository<ApheresisProcedureResult, Long> { //chưa sử dụng
    List<ApheresisProcedureResult> findByPerformedBy_UserId(Long userId);
    List<ApheresisProcedureResult> findByDonation_DonationId(Long donationId);
    List<ApheresisProcedureResult> findByProcedureStartBetween(LocalDateTime start, LocalDateTime end);
}
