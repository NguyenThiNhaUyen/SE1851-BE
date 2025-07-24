package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
>>>>>>> origin/main
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeparationPresetConfigDTO {
    private Long id;
<<<<<<< HEAD
    private String gender;
    private int minWeight;
    private String method;
    private boolean leukoreduced;
    private double rbcRatio;
    private double plasmaRatio;
=======
    @NotBlank(message = "Giới tính không thể trống")
    private String gender;

    @Min(value = 30, message = "Cân nặng tối thiểu là 30 kg")
    private int minWeight;

    @NotBlank(message = "Phương pháp không thể trống")
    private String method;

    private boolean leukoreduced;

    @DecimalMin(value = "0.0", message = "Tỷ lệ RBC không thể là số âm")
    private double rbcRatio;

    @DecimalMin(value = "0.0", message = "Tỷ lệ Plasma không thể là số âm")
    private double plasmaRatio;

    @Min(value = 0, message = "Số lượng tiểu cầu phải là số dương")
>>>>>>> origin/main
    private Integer plateletsFixed;
}
