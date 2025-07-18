package com.quyet.superapp.entity;

<<<<<<< HEAD
=======
import com.quyet.superapp.enums.HealthCheckFailureReason;
>>>>>>> origin/main
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HealthCheckFailureLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false)
    private DonationRegistration registration;

<<<<<<< HEAD
    private String reason;        // Ví dụ: Huyết áp cao, Cân nặng thấp
=======
    private HealthCheckFailureReason reason;        // Ví dụ: Huyết áp cao, Cân nặng thấp
>>>>>>> origin/main
    private String staffNote;     // Ghi chú thêm từ nhân viên y tế

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
