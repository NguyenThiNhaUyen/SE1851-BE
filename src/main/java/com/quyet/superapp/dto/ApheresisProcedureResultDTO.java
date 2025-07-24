package com.quyet.superapp.dto;

import com.quyet.superapp.entity.Donation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApheresisProcedureResultDTO {

    private Long apheresisProcedureResultId;

    @NotNull
    private Long donationId;

    @NotNull
    private Long machineId;

    private LocalDateTime procedureStart;

    private LocalDateTime procedureEnd;

    @Min(0)
    private Integer totalVolumeMl;

    @NotBlank
    private String note;

    @NotBlank
    private String performedBy;
}
