package com.quyet.superapp.dto;

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
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime paymentTime;
    private String transactionCode;
    private String status;
}

