package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodTypeRepository extends JpaRepository<BloodType, Long> {
    // Có thể thêm custom query nếu cần, ví dụ:
    // Optional<BloodType> findByDescription(String description);
}
