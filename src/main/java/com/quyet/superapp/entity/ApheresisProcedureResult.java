package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "apheresis_procedure_results")
public class ApheresisProcedureResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ApheresisProcedureResultID")
    private Long apheresisProcedureResultId;

    @ManyToOne
    @JoinColumn(name = "donation_id")
    private Donation donation;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private ApheresisMachine machine;

    @Column(name = "procedure_start")
    private LocalDateTime procedureStart;

    @Column(name = "procedure_end")
    private LocalDateTime procedureEnd;

    @Column(name = "total_volume_ml")
    private Integer totalVolumeMl;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "performed_by_user_id")
    private User performedBy;

}
