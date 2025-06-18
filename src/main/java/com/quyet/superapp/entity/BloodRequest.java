    package com.quyet.superapp.entity;

    import jakarta.persistence.*;
    import lombok.*;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "BloodRequests")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class BloodRequest {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "BloodRequestID")
        private Long bloodRequestId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "RequesterID")
        private User requester;

        @ManyToOne
        @JoinColumn(name = "BloodType", referencedColumnName = "BloodTypeID")
        private BloodType bloodType;

        @ManyToOne
        @JoinColumn(name = "ComponentID")
        private BloodComponent component;

        @Column(name = "QuantityML")
        private Integer quantityMl;

        @Column(name = "UrgencyLevel", columnDefinition = "NVARCHAR(20)")
        private String urgencyLevel;

        @Column(name = "Status", columnDefinition = "NVARCHAR(20)")
        private String status;

        @Column(name = "CreatedAt", columnDefinition = "DATETIME")
        private LocalDateTime createdAt;

        @Column(name = "ConfirmedVolumeML")
        private Integer confirmedVolumeMl;

    }