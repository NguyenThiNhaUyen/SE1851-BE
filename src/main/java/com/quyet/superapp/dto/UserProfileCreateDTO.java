package com.quyet.superapp.dto;

<<<<<<< HEAD
import lombok.Data;

@Data
public class UserProfileCreateDTO {
    private String fullName;
    private ContactInfoDTO contactInfo;
    private String citizenId;
    private String staffPosition; // ✅ dùng để set là "Doctor"
=======
import com.quyet.superapp.dto.ContactInfoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileCreateDTO {

    @NotBlank(message = "Họ và tên không thể trống")
    private String fullName;

    private LocalDate dob;

    @NotBlank(message = "Giới tính không thể trống")
    private String gender;

    @Valid
    private ContactInfoDTO contactInfo;

    @NotBlank(message = "CCCD không được để trống")
    @Pattern(regexp = "\\d{12}", message = "CCCD phải gồm đúng 12 chữ số")
    private String citizenId;

    private String occupation;

    private Long addressId;

    private AddressDTO address; // optional nếu muốn đẩy thêm dữ liệu

    @Positive(message = "Cân nặng phải là số dương")
    private Double weight;

    @Positive(message = "Chiều cao phải là số dương")
    private Double height;

    private String bloodGroup;

    private boolean hasInsurance;
    private String insuranceCardNumber;
    private LocalDate insuranceValidTo;
>>>>>>> origin/main
}
