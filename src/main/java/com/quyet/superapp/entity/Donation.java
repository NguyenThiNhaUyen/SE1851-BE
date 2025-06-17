        package com.quyet.superapp.entity;

        import jakarta.persistence.*;
        import lombok.*;
        import java.time.LocalDate;

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
        }
