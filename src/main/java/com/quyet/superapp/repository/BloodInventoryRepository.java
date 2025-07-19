package com.quyet.superapp.repository;

<<<<<<< HEAD
import com.quyet.superapp.dto.BloodGroupDistributionDTO;
=======
>>>>>>> origin/main
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.entity.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
<<<<<<< HEAD

import java.util.List;
import java.util.Optional;

public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {

        long countByTotalQuantityMlGreaterThan(int value);

        @Query("SELECT new com.quyet.superapp.dto.BloodGroupDistributionDTO(b.bloodType.description, SUM(b.totalQuantityMl)) " +
                "FROM BloodInventory b WHERE b.totalQuantityMl > 0 GROUP BY b.bloodType.description")
        List<BloodGroupDistributionDTO> calculateBloodGroupDistribution();


        /**
         * ✅ Tìm inventory usable theo nhóm máu + thành phần
         * - Số lượng > 0
         * - Trạng thái STORED
         * - Chưa hết hạn (expiredAt > now hoặc null)
         */
        @Query("SELECT b FROM BloodInventory b WHERE " +
                "b.bloodType.bloodTypeId = :bloodTypeId AND " +
                "b.component.bloodComponentId = :componentId AND " +
                "b.totalQuantityMl > 0 AND " +
                "b.status = 'STORED' AND " +
                "(b.expiredAt IS NULL OR b.expiredAt > CURRENT_TIMESTAMP)")
        List<BloodInventory> findByBloodTypeAndComponentAndUsable(
                @Param("bloodTypeId") Long bloodTypeId,
                @Param("componentId") Long componentId
        );

        /**
         * ✅ Tìm inventory usable theo nhóm máu tương thích và thành phần
         * Dùng trong logic tương thích khi nhóm máu cần → các loại có thể thay thế
         */
        @Query("SELECT b FROM BloodInventory b WHERE " +
                "b.bloodType IN :types AND " +
                "b.component.bloodComponentId = :componentId AND " +
                "b.totalQuantityMl > 0 AND " +
                "b.status = 'STORED' AND " +
                "(b.expiredAt IS NULL OR b.expiredAt > CURRENT_TIMESTAMP)")
        List<BloodInventory> findByBloodTypeInAndComponentAndUsable(
                @Param("types") List<BloodType> types,
                @Param("componentId") Long componentId
        );

        /**
         * ✅ Tìm theo BloodType & Component chính xác
         * (Không kiểm tra usable)
         */
        Optional<BloodInventory> findByBloodTypeAndComponent(BloodType bloodType, BloodComponent component);

        /**
         * ✅ Truy vấn thủ công bằng ID để tránh lỗi entity proxy
         */
        @Query("SELECT bi FROM BloodInventory bi WHERE " +
                "bi.bloodType.bloodTypeId = :bloodTypeId AND " +
                "bi.component.bloodComponentId = :componentId")
        List<BloodInventory> findByTypeAndComponent(
                @Param("bloodTypeId") Long bloodTypeId,
                @Param("componentId") Long componentId
        );

        /**
         * 📊 Thống kê lượng máu theo nhóm máu
         */
=======
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {

        Optional<BloodInventory> findByBloodTypeAndComponent(BloodType bloodType, BloodComponent component);


        // ✅ FIXED - thêm @Query để không bị lỗi property 'type'
        @Query("SELECT bi FROM BloodInventory bi WHERE bi.bloodType.bloodTypeId = :bloodTypeId AND bi.component.bloodComponentId = :componentId")
        Optional<BloodInventory> findByTypeAndComponent(@Param("bloodTypeId") Long bloodTypeId, @Param("componentId") Long componentId);

>>>>>>> origin/main
        @Query("SELECT bi.bloodType.description, SUM(bi.totalQuantityMl) " +
                "FROM BloodInventory bi GROUP BY bi.bloodType.description")
        List<Object[]> findGroupCounts();

<<<<<<< HEAD
        /**
         * 📊 Tổng lượng máu hiện có (mL)
         */
=======
>>>>>>> origin/main
        @Query("SELECT COALESCE(SUM(bi.totalQuantityMl), 0) FROM BloodInventory bi")
        long sumAllUnits();
}
