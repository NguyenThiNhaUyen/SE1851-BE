package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "UserProfile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "User_Id")
    private User user;

    @Column(name = "full_name", columnDefinition = "NVARCHAR(50)")
    private String fullName;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "gender", columnDefinition = "NVARCHAR(10)")
    private String gender;

    @Column(name = "blood_type", columnDefinition = "NVARCHAR(10)")
    private String bloodType;

    @Column(name = "address", columnDefinition = "NVARCHAR(200)")
    private String address;

    @Column(name = "phone", columnDefinition = "VARCHAR(20)")
    private String phone;

    @Column(name = "landline", columnDefinition = "VARCHAR(20)")
    private String landline;

    @Column(name = "email", columnDefinition = "VARCHAR(100)")
    private String email;

    @Column(name = "occupation", columnDefinition = "NVARCHAR(50)")
    private String occupation;

    @Column(name = "donation_date")
    private LocalDate lastDonationDate;

    @Column(name = "recovery_time")
    private Integer recoveryTime;

    @Column(name = "location", columnDefinition = "NVARCHAR(100)")
    private String location;

    @Column(name = "citizen_id", columnDefinition = "VARCHAR(12)", unique = true)
    private String citizenId;

}


