package com.quyet.superapp.entity;

import com.quyet.superapp.enums.BloodUnitStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "BloodUnits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id")
    private Long bloodUnitId;

    @Column(name = "unit_code", unique = true, length = 20, nullable = false)
    private String unitCode;

    @Column(name = "quantity_ml", nullable = false)
    private Integer quantityMl;

    @Column(name = "expiration_date", columnDefinition = "DATE", nullable = false)
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private BloodUnitStatus status;

    @Column(name = "stored_at", columnDefinition = "DATETIME")
    private LocalDateTime storedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BloodType")
    private BloodType bloodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private BloodComponent component;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_bag_id")
    private BloodBag bloodBag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separation_order_id")
    private SeparationOrder separationOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id")
    private Donation donation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id") // để ánh xạ với SeparationResult
    private SeparationResult result;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
