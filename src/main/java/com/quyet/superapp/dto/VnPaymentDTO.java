package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
>>>>>>> origin/main
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
<<<<<<< HEAD

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VnPaymentDTO {
    private Long id;
    private Long requestId;           // ✅ ID của đơn yêu cầu máu
    private Long userId;              // 👤 ID của người thực hiện thanh toán (staff)
    private String userFullName;      // 👤 Tên staff (nếu muốn show rõ hơn)
    private BigDecimal amount;        // 💰 Số tiền
    private LocalDateTime paymentTime; // ⏰ Thời gian thanh toán
    private String transactionCode;   // 🔗 Mã giao dịch
    private String status;            // 📌 Trạng thái: SUCCESS, FAILED, ...
}
=======
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

>>>>>>> origin/main
