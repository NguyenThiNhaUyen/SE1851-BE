package com.quyet.superapp.repository;

import com.quyet.superapp.entity.LabTestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabTestResultRepository extends JpaRepository<LabTestResult, Long> {
    // Tìm theo đơn vị máu
    Optional<LabTestResult> findByBloodUnit_BloodUnitId(Long bloodUnitId);

    // Kiểm tra xem đơn vị máu đã được xét nghiệm chưa
    boolean existsByBloodUnit_BloodUnitId(Long bloodUnitId);


}
