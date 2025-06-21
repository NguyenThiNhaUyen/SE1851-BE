package com.quyet.superapp.repository;

import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.enums.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRegistrationRepository extends JpaRepository<DonationRegistration, Long> {
    // 🔍 Tìm danh sách đơn đăng ký theo trạng thái (PENDING, CONFIRMED, ...)
    List<DonationRegistration> findByStatus(DonationStatus status);

    // 🔍 Tìm đơn đăng ký theo người dùng
    List<DonationRegistration> findByUser_UserId(Long userId);

    // ❗ Kiểm tra user có đơn đăng ký nào ở trạng thái PENDING không (để tránh đăng ký trùng)
    boolean existsByUser_UserIdAndStatus(Long userId, DonationStatus status);

}