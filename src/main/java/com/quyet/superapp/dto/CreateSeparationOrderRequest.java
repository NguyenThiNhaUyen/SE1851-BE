package com.quyet.superapp.dto;

import com.quyet.superapp.enums.SeparationMethod;
import com.quyet.superapp.enums.SeparationPattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSeparationOrderRequest {
    @NotNull(message = "ID túi máu không được để trống")
    private Long bloodBagId;

    @NotNull(message = "ID nhân viên thao tác không được để trống")
    private Long operatorId;

    private Long machineId; // optional nếu là MANUAL

    @NotNull(message = "Phương pháp tách máu không được để trống")
    private SeparationMethod method;

    @NotNull(message = "Kiểu tách máu không được để trống")
    private SeparationPattern pattern;

    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự")
    private String note;
}
