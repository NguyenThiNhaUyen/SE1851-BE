package com.quyet.superapp.dto;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonFormat;
=======
>>>>>>> origin/main
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

<<<<<<< HEAD
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationHistoryDTO {
    private Long id;

    private String donorName; // 👈 Gợi ý thêm nếu bạn muốn hiển thị tên người hiến

    private String bloodType;
    private String componentDonated;
    private Integer volumeMl;
    private String donationLocation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime donatedAt;

    private String status;

    private Boolean paymentRequired;
    private Boolean paymentCompleted;
    private Integer paymentAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    private String paymentMethod;
    private String transactionCode;
=======
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationHistoryDTO {
    private LocalDate donationDate;
    private String location;
    private Integer volumeMl;
    private String bloodGroup;
    private String component;
    private String status;

    // ✅ Thêm 2 trường mới để hiển thị thời gian phục hồi
    private LocalDate recoveryDate;     // Ngày có thể hiến lại
    private boolean isRecovered;        // Đã đủ điều kiện hiến lại?
>>>>>>> origin/main
}
