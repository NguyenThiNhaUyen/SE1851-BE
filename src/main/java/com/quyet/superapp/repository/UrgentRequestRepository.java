package com.quyet.superapp.repository;

import com.quyet.superapp.entity.UrgentRequest;
<<<<<<< HEAD
import com.quyet.superapp.entity.User;
import com.quyet.superapp.enums.BloodRequestStatus;
=======
import com.quyet.superapp.enums.RequestStatus;
>>>>>>> origin/main
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> origin/main

@Repository
public interface UrgentRequestRepository extends JpaRepository<UrgentRequest, Long> {

<<<<<<< HEAD
    Optional<UrgentRequest> findByRequester(User user); // ✅ OK


    // ✅ Lọc theo trạng thái
    List<UrgentRequest> findByStatus(BloodRequestStatus status);
=======
    // ✅ Lọc theo trạng thái
    List<UrgentRequest> findByStatus(RequestStatus status);
>>>>>>> origin/main

    // ✅ Lọc theo người gửi
    List<UrgentRequest> findByRequesterUserId(Long userId);

    // ✅ Thống kê theo trạng thái (ví dụ: Pending bao nhiêu cái)
<<<<<<< HEAD
    long countByStatus(BloodRequestStatus status); // ⚠ Đổi String → enum

    // 🔍 Tìm tất cả yêu cầu PENDING để admin duyệt
    List<UrgentRequest> findAllByStatus(BloodRequestStatus status);
=======
    long countByStatus(RequestStatus status); // ⚠ Đổi String → enum

    // 🔍 Tìm tất cả yêu cầu PENDING để admin duyệt
    List<UrgentRequest> findAllByStatus(RequestStatus status);
>>>>>>> origin/main
}
