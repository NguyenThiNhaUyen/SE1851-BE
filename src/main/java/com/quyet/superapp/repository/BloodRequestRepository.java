    package com.quyet.superapp.repository;

    import com.quyet.superapp.entity.BloodRequest;
    import com.quyet.superapp.entity.User;
    import com.quyet.superapp.enums.BloodRequestStatus;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Modifying;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.time.LocalDateTime;
    import java.util.List;

    public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {

        @Query("""
        SELECT br FROM BloodRequest br
        WHERE br.status = 'APPROVED'
          AND br.paymentStatus != 'SUCCESS'
          AND br.createdAt <= :expiredTime
    """)
        List<BloodRequest> findUnpaidAndExpiredRequests(@Param("expiredTime") LocalDateTime expiredTime);


        // 🔍 Truy vấn yêu cầu máu KHẨN còn hoạt động
        // ✅ Repository:
        @Query("SELECT r FROM BloodRequest r " +
                "WHERE UPPER(r.urgencyLevel) IN ('KHẨN CẤP', 'CẤP CỨU') " +
                "AND r.status IN :statuses")
        List<BloodRequest> findUrgentActiveRequests(@Param("statuses") List<BloodRequestStatus> statuses);


        // 🔍 Truy vấn yêu cầu KHẨN đang chờ duyệt
        @Query("SELECT r FROM BloodRequest r WHERE UPPER(r.urgencyLevel) IN ('KHẨN CẤP', 'CẤP CỨU') AND r.status = 'PENDING'")
        List<BloodRequest> findUrgentPendingRequests();

        // 🔍 Lịch sử tất cả yêu cầu KHẨN
        @Query("SELECT r FROM BloodRequest r WHERE UPPER(r.urgencyLevel) IN ('KHẨN CẤP', 'CẤP CỨU')")
        List<BloodRequest> findUrgentRequestHistory();

        // ✅ Kiểm tra trùng mã bệnh án
        boolean existsByPatientRecordCode(String patientRecordCode);

        // ✅ Kiểm tra bác sĩ có đang phụ trách đơn nào không
        boolean existsByDoctorAndStatusIn(User doctor, List<String> statuses);

        // 📊 Thống kê số đơn trong khoảng thời gian
        long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

        // 🔍 Đơn chưa thanh toán quá thời gian
        @Query("SELECT r FROM BloodRequest r WHERE r.paymentStatus = 'UNPAID' AND r.approvedAt < :cutoff")
        List<BloodRequest> findOverdueUnpaidRequests(@Param("cutoff") LocalDateTime cutoff);

        // ✅ Truy vấn theo trạng thái
        List<BloodRequest> findByStatusIn(List<String> statuses);

        // 🧾 Lấy danh sách đơn đã thanh toán
        @Query("SELECT r FROM BloodRequest r WHERE r.paymentStatus = 'PAID'")
        List<BloodRequest> findAllPaidRequests();

        // 🧾 Lấy danh sách đơn được hoãn thanh toán
        @Query("SELECT r FROM BloodRequest r WHERE r.paymentStatus = 'DEFERRED'")
        List<BloodRequest> findAllDeferredPayments();

        // ❌ Hủy đơn quá hạn chưa thanh toán
        @Modifying
        @Query("UPDATE BloodRequest r SET r.status = 'CANCELLED', r.cancelReason = :reason " +
                "WHERE r.paymentStatus = 'UNPAID' AND r.approvedAt < :cutoff")
        int cancelOverdueUnpaidRequests(@Param("cutoff") LocalDateTime cutoff, @Param("reason") String reason);
    }
