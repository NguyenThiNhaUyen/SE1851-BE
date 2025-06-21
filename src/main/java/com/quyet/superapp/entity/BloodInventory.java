package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "BloodInventory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BloodInventoryID")
    private Long bloodInventoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BloodType",  referencedColumnName = "BloodTypeID")
    private BloodType bloodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ComponentID")
    private BloodComponent component;

    @Column(name = "TotalQuantityML")
    private Integer totalQuantityMl;

    @Column(name = "LastUpdated", columnDefinition = "DATETIME")
    private LocalDateTime lastUpdated;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
