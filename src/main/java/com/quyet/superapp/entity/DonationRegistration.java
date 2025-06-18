package com.quyet.superapp.entity;

import com.quyet.superapp.enums.DonationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "DonationRegistrations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Registration_Id")
    private Long registrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id")
    private User user;

    @Column(name = "ready_date", columnDefinition = "DATE")
    private LocalDate readyDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 20)
    private DonationStatus status;

    @Column(name = "location", columnDefinition = "NVARCHAR(100)")
    private String location;

    @Column(name = "blood_type", columnDefinition = "NVARCHAR(20)")
    private String bloodType;

}
