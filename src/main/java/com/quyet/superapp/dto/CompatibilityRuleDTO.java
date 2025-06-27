package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompatibilityRuleDTO {
    private Long compatibilityRuleId;
    @NotNull(message = "ID nhóm máu người cho không được để trống")
    private Long donorTypeId;

    @NotNull(message = "ID nhóm máu người nhận không được để trống")
    private Long recipientTypeId;

    @NotNull(message = "ID thành phần máu không được để trống")
    private Long componentId;

    @NotNull(message = "Trạng thái tương thích không được để trống")
    private Boolean isCompatible;
}

