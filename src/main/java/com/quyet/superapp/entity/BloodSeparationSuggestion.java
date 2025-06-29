package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_separation_suggestions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodSeparationSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BloodSeparationSuggestionID")
    private Long bloodSeparationSuggestionId;

    @Column(name = "input_data_summary")
    private String inputDataSummary;  // Tổng hợp đầu vào (mô tả hoặc JSON)

    @Column(name = "suggested_config")
    private String suggestedConfig;    // Có thể chứa JSON hoặc đoạn text config

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "generated_by_user_id")
    private User generatedBy;

    @Column(name = "red_cells")
    private Integer redCells;

    @Column(name = "plasma")
    private Integer plasma;

    @Column(name = "platelets")
    private Integer platelets;

    @Column(name = "red_cells_code")
    private String redCellsCode;

    @Column(name = "plasma_code")
    private String plasmaCode;

    @Column(name = "platelets_code")
    private String plateletsCode;

    @Column(name = "description")
    private String description;
}
