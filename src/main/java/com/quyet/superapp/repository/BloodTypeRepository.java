package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BloodTypeRepository extends JpaRepository<BloodType, Long> {

    Optional<BloodType> findByDescription(String description); // ví dụ: "O+"
    Optional<BloodType> findByDescriptionIgnoreCase(String description); // "o+" → "O+"
    List<BloodType> findByDescriptionIn(List<String> descriptions); // ví dụ: ["A+", "B+"]
    List<BloodType> findByDescriptionContaining(String keyword); // chứa chuỗi, ví dụ "O"


}
