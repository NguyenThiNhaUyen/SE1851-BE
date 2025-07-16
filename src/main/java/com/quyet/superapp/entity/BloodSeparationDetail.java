package com.quyet.superapp.entity;

import com.quyet.superapp.enums.BloodComponentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_separation_details")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BloodSeparationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BloodSeparationDetailID")
    private Long bloodSeparationDetailId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "result_id", nullable = false)
    private SeparationResult result;

    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false)
    private BloodComponentType componentType;

    @Column(name = "volume_ml", nullable = false)
    private Integer volumeMl;

    @Column(name = "quality_rating", length = 50)
    private String qualityRating;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
