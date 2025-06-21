package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "BloodSeparationDetail ")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodSeparationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separation_log_id")
    private BloodSeparationLog separationLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_type_id")
    private BloodType bloodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private BloodComponent component;

    @Column(name = "volume_ml")
    private Integer volumeMl;

    @Column(name = "unit_code", columnDefinition = "VARCHAR(20)")
    private String unitCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
