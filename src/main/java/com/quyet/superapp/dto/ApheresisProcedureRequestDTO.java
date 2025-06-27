package com.quyet.superapp.dto;

import com.quyet.superapp.enums.SeparationMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApheresisProcedureRequestDTO {
    // Bệnh nhân/người hiến
    private Long bloodBagId;

    private Long operatorId;

    private Long machineId;

    // Nhập thông tin sinh học
    @NotBlank
    private String gender;
    private Double weight;

    // Phương pháp tách
    private SeparationMethod method; // APHERESIS
    private boolean leukoreduced;

    // Tùy chọn đầu ra (hệ thống có thể override theo preset)
    @Min(0)
    private Integer redCellsMl;
    @Min(0)
    private Integer plasmaMl;
    @Min(0)
    private Integer plateletsMl;

    private String note;
}
