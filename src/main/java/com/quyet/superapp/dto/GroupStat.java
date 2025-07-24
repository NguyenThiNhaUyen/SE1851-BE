package com.quyet.superapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupStat {
    @NotBlank(message = "Nhóm máu không thể trống")
    private String bloodGroup;

    @Min(value = 0, message = "Số lượng đơn vị máu phải là số dương")
    private long unitCount;
}
