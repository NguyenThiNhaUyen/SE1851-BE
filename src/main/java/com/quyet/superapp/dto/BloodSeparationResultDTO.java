package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodSeparationResultDTO {
    private Long separationId;
    private List<BloodSeparationDetailDTO> components;
    private String performedBy;
    private LocalDateTime separatedAt;
}
