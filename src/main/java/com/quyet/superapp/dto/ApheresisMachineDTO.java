package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
>>>>>>> origin/main
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApheresisMachineDTO {
<<<<<<< HEAD
    private Long id;
    private String serialNumber;
    private String manufacturer;
    private String model;
    private boolean isActive;
    private LocalDate lastMaintenance;
=======
    @NotNull
    private Long id;

    @NotBlank
    private String serialNumber;

    @NotBlank
    private String manufacturer;

    @NotBlank
    private String model;

    private boolean isActive;

    @Min(0)
    private LocalDate lastMaintenance;

    @NotBlank
>>>>>>> origin/main
    private String note;
}
