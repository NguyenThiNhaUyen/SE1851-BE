package com.quyet.superapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "BloodComponents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Blood_Component_ID")
    private Long bloodComponentId;

    @Column(name = "NameBloodComponent", columnDefinition = "NVARCHAR(50)")
    private String name;

    @Column(name = "Code", columnDefinition = "VARCHAR(10)")
    private String code; // PRC, FFP, PLT...

    @Column(name = "StorageTemp", columnDefinition = "VARCHAR(20)")
    private String storageTemp; // ví dụ "2-6°C"

    @Column(name = "StorageDays")
    private Integer storageDays; // ví dụ 42

    @Column(name = "Usage", columnDefinition = "NVARCHAR(200)")
    private String usage; // mục đích sử dụng, như "truyền thiếu máu"

    @Column(name = "ApheresisCompatible")
    private Boolean isApheresisCompatible;

    @OneToMany(mappedBy = "component")
    @JsonIgnore
    private List<CompatibilityRule> compatibilityRules;

    @OneToMany(mappedBy = "component")
    private List<Donation> donations;

    @OneToMany(mappedBy = "component")
    private List<BloodUnit> bloodUnits;

    @OneToMany(mappedBy = "component")
    private List<BloodInventory> inventories;

    @OneToMany(mappedBy = "component")
    private List<BloodRequest> requests;
}