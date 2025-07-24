package com.quyet.superapp.entity;

import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.enums.DonorReadinessLevel;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "DonationRegistrations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Registration_Id")
    private Long registrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id")
    private User user;

    @Column(name = "ready_date")
    private LocalDate readyDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 20)
    private DonationStatus status;

    @Column(name = "location", columnDefinition = "NVARCHAR(100)")
    private String location;

    @ManyToOne
    @JoinColumn(name = "blood_type")
    private BloodType bloodType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmed_by")
    private User confirmedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Trong DonationRegistration entity
    @ManyToOne
    @JoinColumn(name = "slot_id")
    private DonationSlot slot;

    @Column(name = "estimated_volume")
    private Integer estimatedVolume;

    @Column(name = "note", columnDefinition = "NVARCHAR(255)")
    private String note;

    // ✅ MỚI: Mức độ sẵn sàng hiến máu
    @Enumerated(EnumType.STRING)
    @Column(name = "readiness_level", length = 30)
    private DonorReadinessLevel readinessLevel;


}
