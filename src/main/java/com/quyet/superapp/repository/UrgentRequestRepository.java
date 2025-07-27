package com.quyet.superapp.repository;

import com.quyet.superapp.entity.UrgentRequest;
import com.quyet.superapp.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrgentRequestRepository extends JpaRepository<UrgentRequest, Long> {

    // ✅ Lọc theo trạng thái
    List<UrgentRequest> findByStatus(RequestStatus status);

    // ✅ Lọc theo người gửi
    List<UrgentRequest> findByRequesterUserId(Long userId);

    // ✅ Thống kê theo trạng thái (ví dụ: Pending bao nhiêu cái)
    long countByStatus(RequestStatus status); // ⚠ Đổi String → enum


}
