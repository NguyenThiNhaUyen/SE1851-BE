package com.quyet.superapp.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Long userId;

    @NotBlank
    private String fullName;
    private LocalDate dob;
    // Chuỗi dd-MM-yyyy
    private String gender;
    private Long bloodTypeId;  // để gửi từ FE là ID nhóm máu
    private String phone;

    private String landline;
    private String email;
    private String occupation;
    private LocalDateTime lastDonationDate;
    private Integer recoveryTime;
    private String location;

    @NotBlank(message = "CCCD không được để trống")
    @Pattern(regexp = "\\d{12}", message = "CCCD phải gồm đúng 12 chữ số")
    private String citizenId;

    private Double weight;
    private Double height;

    private Long addressId;         // ID nếu đã chọn địa chỉ cụ thể
    private String addressFull; // VD: "12 Nguyễn Trãi, Phường 1, Quận 5, TP Hồ Chí Minh"
    private AddressDTO address;  // ✅ Thêm dòng này
    private Double latitude;
    private Double longitude;

}
