package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "separation_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeparationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SeparationResultID")
    private Long separationResultId;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    private SeparationOrder order;

    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL)
    private List<BloodSeparationDetail> details;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "processed_by_user_id")
    private User processedBy;

    @Column(name = "note")
    private String note;
}
