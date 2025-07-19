package com.quyet.superapp.entity;

import com.quyet.superapp.enums.DonationStatus;
import jakarta.persistence.*;
import lombok.*;
<<<<<<< HEAD
=======

>>>>>>> origin/main
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donation {
<<<<<<< HEAD
=======

>>>>>>> origin/main
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "Donation_Id")
        private Long donationId;

<<<<<<< HEAD
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "User_Id")
        private User user; // ❓ Optional nếu đã có trong registration
=======
        // ❗ Optional nếu luôn lấy từ registration
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "User_Id")
        private User user;
>>>>>>> origin/main

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "registration_id")
        private DonationRegistration registration;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "blood_type", referencedColumnName = "BloodTypeID")
        private BloodType bloodType;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "component_id")
<<<<<<< HEAD
        private BloodComponent bloodComponent; // ✅ Gợi ý đặt lại tên


        @Column(name = "donation_time")
        private LocalDateTime donationDate; // ✅ Thay vì chỉ có ngày
=======
        private BloodComponent component; // Có thể null nếu chưa phân tách

        @Column(name = "collected_at")
        private LocalDate collectedAt;
>>>>>>> origin/main

        @Column(name = "volume_ml")
        private Integer volumeMl;

        @Column(name = "location", columnDefinition = "NVARCHAR(50)")
        private String location;

        @Column(name = "notes", columnDefinition = "NVARCHAR(200)")
        private String notes;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Enumerated(EnumType.STRING)
        @Column(name = "status", columnDefinition = "NVARCHAR(20)")
        private DonationStatus status;

<<<<<<< HEAD

=======
        // ✅ Staff xử lý hiến máu (xác nhận, nhập kết quả...)
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "handled_by_staff_id")
        private User handledBy;

        // ✅ Túi máu được sinh ra từ lần hiến này
        @OneToOne
        @JoinColumn(name = "blood_bag_id")
        private BloodBag bloodBag;

        // ✅ Các đơn vị máu được tách ra từ túi máu của lần hiến này
>>>>>>> origin/main
        @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL)
        private List<BloodUnit> bloodUnits;

        @PrePersist
        protected void onCreate() {
                this.createdAt = LocalDateTime.now();
                this.updatedAt = LocalDateTime.now();
<<<<<<< HEAD
                if (this.donationDate == null) {
                        this.donationDate = LocalDateTime.now();
=======
                if (this.collectedAt == null) {
                        this.collectedAt = LocalDate.now();
>>>>>>> origin/main
                }
        }

        @PreUpdate
        protected void onUpdate() {
                this.updatedAt = LocalDateTime.now();
        }
<<<<<<< HEAD

        @Column(name = "recovered_at")
        private LocalDate recoveryDate;

        @Column(name = "is_emergency")
        private Boolean isEmergency;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "separated_component_id")
        private BloodComponent separatedComponent; // 🧪 Thành phần máu thực tế được tách ra

=======
>>>>>>> origin/main
}
