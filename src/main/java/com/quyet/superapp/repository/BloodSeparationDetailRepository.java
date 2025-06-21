package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodSeparationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodSeparationDetailRepository extends JpaRepository<BloodSeparationDetail,Long> {
    List<BloodSeparationDetail> findBySeparationLog_BloodSeparationLogId(Long logId);

    List<BloodSeparationDetail> findByBloodType_BloodTypeId(Long bloodTypeId);

    List<BloodSeparationDetail> findByComponent_BloodComponentId(Long componentId);
}
