package com.quyet.superapp.dto;

import com.quyet.superapp.entity.BloodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodTypeDTO {

    private Long bloodTypeId;

    @NotBlank(message = "Mô tả nhóm máu không được để trống")
    @Size(max = 10, message = "Mô tả nhóm máu không được vượt quá 10 ký tự")
    private String description;

}
