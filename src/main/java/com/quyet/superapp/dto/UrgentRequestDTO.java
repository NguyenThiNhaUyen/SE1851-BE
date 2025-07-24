package com.quyet.superapp.dto;

<<<<<<< HEAD
import com.quyet.superapp.enums.BloodRequestStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UrgentRequestDTO {
    private Long urgentRequestId;
    private String hospitalName;
    private String bloodType;
    private int units;
    private LocalDate requestDate;
    private BloodRequestStatus status;
=======
import com.quyet.superapp.enums.RequestStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UrgentRequestDTO {
    @NotNull(message = "Mã yêu cầu không thể null")
    private Long urgentRequestId;

    @NotBlank(message = "Tên bệnh viện không thể trống")
    private String hospitalName;

    @NotBlank(message = "Nhóm máu không thể trống")
    private String bloodType;

    @Min(value = 1, message = "Số lượng yêu cầu phải ít nhất là 1 đơn vị máu")
    private int units;

    @NotNull(message = "Ngày yêu cầu không thể null")
    private LocalDate requestDate;

    @NotNull(message = "Trạng thái không thể null")
    private RequestStatus status;

    @NotNull(message = "ID người yêu cầu không thể null")
>>>>>>> origin/main
    private Long requesterId;
}

