package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
>>>>>>> origin/main
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupStat {
<<<<<<< HEAD
    private String bloodGroup;
=======
    @NotBlank(message = "Nhóm máu không thể trống")
    private String bloodGroup;

    @Min(value = 0, message = "Số lượng đơn vị máu phải là số dương")
>>>>>>> origin/main
    private long unitCount;
}
