package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.enums.RhType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BloodTypeRepository extends JpaRepository<BloodType, Long> {
    // Có thể thêm custom query nếu cần, ví dụ:

    // ✅ Repository
    Optional<BloodType> findByDescription(String description);
}
