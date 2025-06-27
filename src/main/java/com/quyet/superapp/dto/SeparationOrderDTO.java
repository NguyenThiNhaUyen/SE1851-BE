package com.quyet.superapp.dto;

import com.quyet.superapp.enums.SeparationMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeparationOrderDTO {
    @NotNull(message = "Separation order ID không thể null")
    private Long separationOrderId;

    @NotNull(message = "Blood bag ID không thể null")
    private Long bloodBagId; // Túi máu được tách

    @NotNull(message = "ID nhân viên thực hiện không thể null")
    private Long performedById; // Nhân viên thực hiện

    @NotNull(message = "ID máy tách không thể null")
    private Long apheresisMachineId; // 👈 MÁY ĐÃ DÙNG ĐỂ TÁCH

    @NotNull(message = "Ngày thực hiện không thể null")
    private LocalDateTime performedAt;

    @NotNull(message = "Phương pháp tách máu không thể null")
    private SeparationMethod separationType;

    private String note;
}
