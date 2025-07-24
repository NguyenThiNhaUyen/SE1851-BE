package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
>>>>>>> origin/main
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodRequestConfirmDTO {
<<<<<<< HEAD
    private Long requestId;
    private Integer confirmedVolumeMl; // ✅ lượng máu staff thực tế nhận
=======
    @NotNull(message = "requestId không được để trống")
    private Long requestId;

    @NotNull(message = "Lượng máu xác nhận không được để trống")
    @Min(value = 1, message = "Lượng máu xác nhận phải > 0ml")
    private Integer confirmedVolumeMl;
>>>>>>> origin/main
}
