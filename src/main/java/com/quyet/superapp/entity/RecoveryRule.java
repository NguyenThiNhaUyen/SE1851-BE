package com.quyet.superapp.entity;

import com.quyet.superapp.enums.BloodComponentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RecoveryRules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoveryRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Recovery_Rule_Id")
    private Long recoveryRuleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "Blood_Component", nullable = false, unique = true)
    private BloodComponentType componentType; // e.g., PLASMA, PLATELET, RBC

    @Column(name = "Recovery_Days", nullable = false)
    private int recoveryDays; // Số ngày phục hồi khuyến nghị

    @Column(name = "Note")
    private String note;
}
