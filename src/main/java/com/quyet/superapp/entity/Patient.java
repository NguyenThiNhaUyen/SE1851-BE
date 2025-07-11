package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phone;
    private String gender;
    private Integer age;
    private Double weight;

    private String bloodGroup;   // Ví dụ: "O+", "A-"
    private String citizenId;    // CCCD

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_user_id")
    private User linkedUser;     // Optional: nếu bệnh nhân là thành viên hệ thống

    private LocalDateTime createdAt;
}
