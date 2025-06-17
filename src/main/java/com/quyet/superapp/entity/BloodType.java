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

    @Column(name = "Description", columnDefinition = "NVARCHAR(20)")
    private String description;

    @OneToMany(mappedBy = "bloodType")
    @JsonIgnore
    private List<Donation> donations;

    @OneToMany(mappedBy = "bloodType")
    private List<BloodUnit> bloodUnits;

    @OneToMany(mappedBy = "bloodType")
    private List<BloodInventory> bloodInventories;

    @OneToMany(mappedBy = "bloodType")
    private List<BloodRequest> bloodRequests;

    @OneToMany(mappedBy = "donorType")
    private List<CompatibilityRule> donorRules;

    @OneToMany(mappedBy = "recipientType")
    private List<CompatibilityRule> recipientRules;
}
