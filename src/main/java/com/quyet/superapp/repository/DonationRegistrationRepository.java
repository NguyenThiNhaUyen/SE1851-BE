package com.quyet.superapp.repository;

import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.enums.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DonationRegistrationRepository extends JpaRepository<DonationRegistration, Long> {
    // 🔍 Tìm danh sách đơn đăng ký theo trạng thái (PENDING, CONFIRMED, ...)
    List<DonationRegistration> findByStatus(DonationStatus status);


    // ❗ Kiểm tra user có đơn đăng ký nào ở trạng thái PENDING không (để tránh đăng ký trùng)
    boolean existsByUser_UserIdAndStatus(Long userId, DonationStatus status);

    // Phương thức này cần tham chiếu đến thuộc tính đúng là `slot`
    List<DonationRegistration> findBySlot_SlotId(Long slotId);


}