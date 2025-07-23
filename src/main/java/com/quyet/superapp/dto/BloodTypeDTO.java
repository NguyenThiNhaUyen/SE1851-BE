package com.quyet.superapp.dto;

<<<<<<< HEAD
import jakarta.validation.constraints.NotBlank;
=======
import com.quyet.superapp.entity.BloodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
>>>>>>> origin/main
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
<<<<<<< HEAD
@Builder
public class BloodTypeDTO {
    private Long bloodTypeId;

    @NotBlank(message = "Mô tả nhóm máu không được để trống")
    private String description; // ví dụ: "O+"

    private String rh;          // "+" hoặc "-"
    private String note;        // Ghi chú (phổ biến, toàn năng...)
    private Boolean isActive;   // true/false
=======
public class BloodTypeDTO {

    private Long bloodTypeId;

    @NotBlank(message = "Mô tả nhóm máu không được để trống")
    @Size(max = 10, message = "Mô tả nhóm máu không được vượt quá 10 ký tự")
    private String description;

>>>>>>> origin/main
}
