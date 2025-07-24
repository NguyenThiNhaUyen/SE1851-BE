package com.quyet.superapp.dto;

import com.quyet.superapp.enums.DonationStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRequestDTO {
    @NotNull(message = "Donation ID không thể null")
    private Long donationId;

    @NotNull(message = "User ID không thể null")
    private Long userId;

    @NotNull(message = "Registration ID không thể null")
    private Long registrationId;

    @NotNull(message = "Blood Type ID không thể null")
    private Long bloodTypeId;

    @NotNull(message = "Component ID không thể null")
    private Long componentId;

    @FutureOrPresent(message = "Ngày hiến máu phải là ngày hiện tại hoặc tương lai")
    private LocalDate donationDate;

    @NotNull(message = "Khối lượng máu (ml) không thể null")
    @Min(value = 10, message = "Khối lượng phải tối thiểu 10 ml")
    private Integer volumeMl;

    @NotBlank(message = "Địa điểm không thể trống")
    private String location;

    private String notes;

    @NotNull(message = "Cân nặng không thể null")
    @Min(value = 30, message = "Cân nặng phải tối thiểu 30 kg")
    @Max(value = 200, message = "Cân nặng không được quá 200 kg")
    private Double weight;

    @NotNull(message = "Trạng thái hiến máu không thể null")
    private DonationStatus status;

    @NotNull(message = "Ngày tạo không thể null")
    private LocalDateTime createdAt;

    @NotNull(message = "Ngày cập nhật không thể null")
    private LocalDateTime updatedAt;
}


