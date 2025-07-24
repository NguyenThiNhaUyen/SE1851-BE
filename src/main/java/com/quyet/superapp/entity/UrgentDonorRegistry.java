package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "UrgentDonorRegistry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrgentDonorRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User donor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_type_id")
    private BloodType bloodType;

    @Column(name = "location", columnDefinition = "NVARCHAR(100)")
    private String location;

    @Column(name = "is_available")
    private Boolean isAvailable;  // ✅ Sửa từ primitive boolean sang Boolean

    @Column(name = "last_contacted")
    private LocalDateTime lastContacted;
}
