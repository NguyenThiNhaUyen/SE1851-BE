package com.quyet.superapp.repository;

import com.quyet.superapp.entity.SeparationOrder;
import com.quyet.superapp.enums.SeparationMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeparationOrderRepository extends JpaRepository<SeparationOrder, Long> {
    // 🔍 Tìm theo loại tách máu (LY_TAM, GAN_TACH, ...)
    List<SeparationOrder> findBySeparationMethod(SeparationMethod method);

    // 📅 Tìm các thao tác tách máu thực hiện trong khoảng thời gian
    List<SeparationOrder> findByPerformedAtBetween(LocalDateTime start, LocalDateTime end);

    // 👤 Tìm tất cả thao tác của 1 nhân viên theo ID
    List<SeparationOrder> findByPerformedBy_UserId(Long userId);

    // 📦 Tìm các tách máu thực hiện từ túi máu cụ thể
    List<SeparationOrder> findByBloodBag_BagCode(String bagCode);

    // ✅ Kiểm tra đã tách máu từ túi nào đó chưa
    boolean existsByBloodBag_BloodBagId(Long bloodBagId);

    @Query("SELECT FUNCTION('DATE_FORMAT', o.performedAt, '%Y-%m-%d'), COUNT(o) " +
            "FROM SeparationOrder o " +
            "GROUP BY FUNCTION('DATE_FORMAT', o.performedAt, '%Y-%m-%d')")
    List<Object[]> countSeparationByDay();

    @Query("SELECT o.bloodBag.bloodType.description, COUNT(o) " +
            "FROM SeparationOrder o " +
            "GROUP BY o.bloodBag.bloodType.description")
    List<Object[]> countByBloodType();
}
