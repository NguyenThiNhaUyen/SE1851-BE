package com.quyet.superapp.dto;

import com.quyet.superapp.enums.SeparationMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CreateSeparationWithSuggestionRequest {
    @NotNull
    private Long bloodBagId;
    @NotNull
    private Long operatorId;
    private Long machineId;
    @NotNull
    private SeparationMethod type;
    @NotNull
    private String gender;
    @Min(30)
    private Double weight;
    private boolean leukoreduced;
    @Size(max = 200)
    private String note;

}
