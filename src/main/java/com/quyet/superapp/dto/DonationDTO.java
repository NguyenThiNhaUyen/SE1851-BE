package com.quyet.superapp.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationDTO {

    private Long donationId;

    @NotNull(message = "ID đăng ký hiến máu không được để trống")
    private Long registrationId;

    @NotNull(message = "Thể tích máu không được để trống")
    @Min(value = 200, message = "Thể tích máu tối thiểu là 200ml")
    @Max(value = 550, message = "Thể tích máu tối đa là 550ml")
    private Integer volume; // Đơn vị: ml

    @NotNull(message = "Thời gian lấy máu không được để trống")
    private LocalDate collectedAt;

    private String note;

    private String status; // Ví dụ: COMPLETED, CANCELLED...

    private Long staffId;

    // Optional: thông tin đơn đăng ký
    private DonationRegistrationDTO registration;

    // Optional: thông tin người hiến
    private UserDTO user;

    // Optional: gắn túi máu nếu đã sinh
    private String bloodBagCode;

}
