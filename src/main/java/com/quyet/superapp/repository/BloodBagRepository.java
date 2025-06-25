package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodBag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BloodBagRepository extends JpaRepository<BloodBag, Long> {
    // 🔍 Tìm theo mã túi máu
    Optional<BloodBag> findByBagCode(String bagCode);

    // ✅ Có thể mở rộng:
    // List<BloodBag> findByStatus(BloodBagStatus status);
    // List<BloodBag> findByTestStatus(TestStatus testStatus);
}
