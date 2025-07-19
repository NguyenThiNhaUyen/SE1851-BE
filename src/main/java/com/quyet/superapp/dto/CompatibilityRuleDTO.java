package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotNull;
>>>>>>> origin/main
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompatibilityRuleDTO {
    private Long compatibilityRuleId;
<<<<<<< HEAD
    private Long donorTypeId;
    private Long recipientTypeId;
    private Long componentId;
=======
    @NotNull(message = "ID nhóm máu người cho không được để trống")
    private Long donorTypeId;

    @NotNull(message = "ID nhóm máu người nhận không được để trống")
    private Long recipientTypeId;

    @NotNull(message = "ID thành phần máu không được để trống")
    private Long componentId;

    @NotNull(message = "Trạng thái tương thích không được để trống")
>>>>>>> origin/main
    private Boolean isCompatible;
}

