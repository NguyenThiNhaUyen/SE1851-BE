package com.quyet.superapp.dto;

import com.quyet.superapp.entity.BloodType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRegistrationDTO {

    private Long registrationId;

    @NotNull(message = "Thời gian hẹn không được để trống")
    private LocalDate scheduledDate;

    @NotBlank(message = "Địa điểm không được để trống")
    private String location;

    @NotBlank(message = "Nhóm máu không được để trống")
    private BloodType bloodType;

    private String status;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate dob;

    @NotBlank(message = "Giới tính không được để trống")
    private String gender;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{9,12}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    private Long addressId;
    private String addressFull;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String email;
    private Long slotId;
    private DonationSlotDTO slot;

    // ✅ Gợi ý bổ sung:
    private String note;

    @Min(200)
    @Max(550)
    private Integer estimatedVolume; // (ml)
}
