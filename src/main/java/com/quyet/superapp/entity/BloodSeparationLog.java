package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "BloodSeparationLog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodSeparationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BloodSeparationLogID")
    private Long bloodSeparationLogId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Donation_Id")
    private Donation donation;

    @Column(name = "method", columnDefinition = "NVARCHAR(20)") // gạn tách, ly tâm, dùng để lựa
                                                                // chọn phương thức tách
    private String method;

    @Column(name = "total_volume")
    private int totalVolume;

    @Column(name = "red_cells_ml")
    private int redCellsMl;

    @Column(name = "plasma_ml")
    private int plasmaMl;

    @Column(name = "platelets_ml")
    private int plateletsMl;

    @Column(name = "leukoreduced")
    private boolean leukoreduced;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id") // chính xác với bảng Users
    private User performedBy;

    @Column(name = "separation_time")
    private LocalDateTime separatedAt;

    @Column(name = "recovery_time")
    private LocalDate recoveryTime;
}
