package com.quyet.superapp.dto;

<<<<<<< HEAD
import lombok.Data;

@Data
public class ContactInfoDTO {
    private String email;
    private String phone;
=======
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Thông tin liên hệ của người dùng.
 * Có thể tái sử dụng trong Create/Update/Response DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfoDTO {

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    // Optional
    private String landline;

    private String emergencyContact;
    private String altPhone;

    private boolean hasInsurance;
    private String insuranceCardNumber;
    private LocalDate insuranceValidTo;
>>>>>>> origin/main
}
