package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BloodType", referencedColumnName = "BloodTypeID")
    private BloodType bloodType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private BloodComponent component;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id")
    private Donation donation;

    @Column(name = "quantity_ml")
    private Integer quantityMl;

    @Column(name = "expiration_date", columnDefinition = "DATE")
    private LocalDate expirationDate;

    @Column(name = "status", columnDefinition = "NVARCHAR(20)")
    private String status;

    @Column(name = "stored_at", columnDefinition = "DATETIME")
    private LocalDateTime storedAt;
}
