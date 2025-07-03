package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSeparationOrderRequest {

    @NotNull(message = "ID nhân viên thao tác không được để trống")
    private Long operatorId;

    private String note;

}
