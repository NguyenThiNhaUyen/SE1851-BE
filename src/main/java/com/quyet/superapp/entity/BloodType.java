package com.quyet.superapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "BloodTypes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BloodTypeID")
    private Long bloodTypeId;

    @Column(name = "Description", columnDefinition = "NVARCHAR(10)", nullable = false)
    private String description; // VD: A+, B-, AB+

    @OneToMany(mappedBy = "bloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Donation> donations;

    @OneToMany(mappedBy = "bloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BloodUnit> bloodUnits;

    @OneToMany(mappedBy = "bloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BloodInventory> bloodInventories;

    @OneToMany(mappedBy = "bloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BloodRequest> bloodRequests;

    @OneToMany(mappedBy = "donorType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CompatibilityRule> donorRules;

    @OneToMany(mappedBy = "recipientType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CompatibilityRule> recipientRules;
}
