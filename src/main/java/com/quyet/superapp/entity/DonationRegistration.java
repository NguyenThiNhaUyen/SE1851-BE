package com.quyet.superapp.entity;

import com.quyet.superapp.enums.DonationStatus;
<<<<<<< HEAD
=======
import com.quyet.superapp.enums.DonorReadinessLevel;
>>>>>>> origin/main
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
<<<<<<< HEAD
    private LocalDateTime readyDate;
=======
    private LocalDate readyDate;
>>>>>>> origin/main

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 20)
    private DonationStatus status;

    @Column(name = "location", columnDefinition = "NVARCHAR(100)")
    private String location;

<<<<<<< HEAD
    @Column(name = "blood_type", columnDefinition = "NVARCHAR(20)")
    private String bloodType;
=======
    @ManyToOne
    @JoinColumn(name = "blood_type")
    private BloodType bloodType;
>>>>>>> origin/main

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

<<<<<<< HEAD
=======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmed_by")
    private User confirmedBy;

>>>>>>> origin/main
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

<<<<<<< HEAD
    @Column(name = "is_emergency")
    private Boolean isEmergency;
=======
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

>>>>>>> origin/main

}
