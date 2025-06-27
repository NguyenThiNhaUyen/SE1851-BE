package com.quyet.superapp.entity;

import com.quyet.superapp.enums.BloodComponentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "result_id")
    private SeparationResult result;

    @Enumerated(EnumType.STRING)
    private BloodComponentType componentType;

    @Column(name = "volume_ml")
    private Integer volumeMl;

    @Column(name = "quality_rating")
    private String qualityRating;


}
