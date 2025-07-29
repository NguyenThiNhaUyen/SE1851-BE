package com.quyet.superapp.repository;


import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.entity.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {

        long countByTotalQuantityMlGreaterThan(int value);




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
}
