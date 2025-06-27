package com.quyet.superapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodComponentDTO {
    @NotNull
    private Long bloodComponentId;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotBlank
    private String storageTemp;

    @Min(0)
    private Integer storageDays;

    @NotBlank
    private String usage;

    private Boolean isApheresisCompatible;
}
