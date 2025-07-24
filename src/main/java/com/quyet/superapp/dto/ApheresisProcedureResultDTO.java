package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import com.quyet.superapp.entity.Donation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
>>>>>>> origin/main
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApheresisProcedureResultDTO {
<<<<<<< HEAD
    private Long separationOrderId;
    private String operator;
    private String machineSerial;
    private LocalDateTime performedAt;

    private Integer redCells;
    private Integer plasma;
    private Integer platelets;

    private String unitCodesCombined;
    private String note;
=======

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
>>>>>>> origin/main
}
