package com.quyet.superapp.dto;

<<<<<<< HEAD
import lombok.Data;

@Data
public class AddressRequestDTO {
    private String addressStreet;
    private Long wardId;

    // ✅ Bổ sung tọa độ vị trí
    private Double latitude;
    private Double longitude;
=======
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AddressRequestDTO {
    @NotBlank(message = "Địa chỉ cụ thể không được để trống")
    private String addressStreet;

    private Long wardId;
>>>>>>> origin/main
}
