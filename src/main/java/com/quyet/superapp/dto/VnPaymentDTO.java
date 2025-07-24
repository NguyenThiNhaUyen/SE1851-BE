package com.quyet.superapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VnPaymentDTO {
    private Long id;
    @NotNull(message = "User ID không thể null")
    private Long userId;

    @NotNull(message = "Số tiền không thể null")
    @DecimalMin(value = "0.01", message = "Số tiền phải lớn hơn 0")
    private BigDecimal amount;

    @NotNull(message = "Thời gian thanh toán không thể null")
    private LocalDateTime paymentTime;

    @NotBlank(message = "Mã giao dịch không thể trống")
    private String transactionCode;

    @NotBlank(message = "Trạng thái thanh toán không thể trống")
    private String status;
}

