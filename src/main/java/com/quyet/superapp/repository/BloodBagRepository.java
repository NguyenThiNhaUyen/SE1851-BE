package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BloodBagRepository extends JpaRepository<BloodBag, Long> {
    // 🔍 Tìm theo mã túi máu
    Optional<BloodBag> findByBagCode(String bagCode);


}
