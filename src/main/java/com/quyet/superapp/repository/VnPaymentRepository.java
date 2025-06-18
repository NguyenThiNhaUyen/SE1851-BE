package com.quyet.superapp.repository;

import com.quyet.superapp.entity.VnPayment;
import org.springframework.data.jpa.repository.JpaRepository;



import org.springframework.stereotype.Repository;

@Repository

public interface VnPaymentRepository extends JpaRepository<VnPayment, Long> {
    long countByStatus(String status);
}