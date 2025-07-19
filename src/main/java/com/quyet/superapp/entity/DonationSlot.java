package com.quyet.superapp.entity;

import com.quyet.superapp.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "donation_slots")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DonationSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long slotId;

    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "location", columnDefinition = "NVARCHAR(100)")
    private String location;

    @Column(name = "notes", columnDefinition = "NVARCHAR(255)")
    private String notes;

    // Số người đã đăng ký (có thể cập nhật realtime hoặc query phụ)
    @Column(name = "registered_count")
    private Integer registeredCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private SlotStatus status;

    // Nếu cần quan hệ 1-n với DonationRegistration (optional)
    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL)
    private List<DonationRegistration> registrations;

    @Transient
    public int getAvailableCapacity() {
        return this.maxCapacity - (this.registeredCount != null ? this.registeredCount : 0);
    }

    public void decreaseAvailableCapacity() {
        if (this.registeredCount == null) this.registeredCount = 0;
        if (this.registeredCount < this.maxCapacity) {
            this.registeredCount++;
        }
    }
}
