package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {

        List<BloodInventory> findByBloodType_Description(String description);

        // ✅ FIXED - thêm @Query để không bị lỗi property 'type'
        @Query("SELECT bi FROM BloodInventory bi WHERE bi.bloodType.bloodTypeId = :bloodTypeId AND bi.component.bloodComponentId = :componentId")
        Optional<BloodInventory> findByTypeAndComponent(@Param("bloodTypeId") Long bloodTypeId, @Param("componentId") Long componentId);

        @Query("SELECT bi.bloodType.description, SUM(bi.totalQuantityMl) " +
                "FROM BloodInventory bi GROUP BY bi.bloodType.description")
        List<Object[]> findGroupCounts();

        @Query("SELECT COALESCE(SUM(bi.totalQuantityMl), 0) FROM BloodInventory bi")
        long sumAllUnits();
}
