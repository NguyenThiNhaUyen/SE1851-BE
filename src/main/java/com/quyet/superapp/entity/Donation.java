package com.quyet.superapp.entity;

import com.quyet.superapp.enums.DonationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donation {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "Donation_Id")
        private Long donationId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "User_Id")
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "registration_id")
        private DonationRegistration registration;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "blood_type", referencedColumnName = "BloodTypeID")
        private BloodType bloodType;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "component_id")
        private BloodComponent component;

        @Column(name = "donation_date", columnDefinition = "DATE")
        private LocalDate donationDate;

        @Column(name = "volume_ml")
        private Integer volumeMl;

        @Column(name = "location", columnDefinition = "NVARCHAR(20)")
        private String location;

        @Column(name = "notes", columnDefinition = "NVARCHAR(200)")
        private String notes;

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

        @Enumerated(EnumType.STRING)
        @Column(name = "status", columnDefinition = "NVARCHAR(20)")
        private DonationStatus status;

        @OneToOne(mappedBy = "donation", fetch = FetchType.LAZY)
        private BloodSeparationLog bloodSeparationLog;

        @OneToMany(mappedBy = "donation")
        private List<BloodUnit> bloodUnits;

        }
