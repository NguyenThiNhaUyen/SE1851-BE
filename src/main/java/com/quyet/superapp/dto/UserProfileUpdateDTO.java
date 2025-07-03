package com.quyet.superapp.dto;

import com.quyet.superapp.entity.BloodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateDTO {
    @NotNull(message = "ID người dùng không thể để trống")
    private Long userId;

    @NotBlank(message = "Họ và tên không thể trống")
    private String fullName;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate dob;

    @NotBlank(message = "Giới tính không thể trống")
    private String gender;

    @NotBlank(message = "Nhóm máu không thể trống")
    private BloodType bloodType;

    @NotBlank(message = "Số điện thoại không thể trống")
    private String phone;

    @NotBlank(message = "CCCD không được để trống")
    @Pattern(regexp = "\\d{12}", message = "CCCD phải gồm đúng 12 chữ số")
    private String citizenId;

    private String landline;
    private String email;
    private String occupation;
    private LocalDate lastDonationDate;
    private Integer recoveryTime;
    private String location;
    private Double weight;
    private Double height;

    private Long addressId;
    private AddressDTO address;
    private Double latitude;
    private Double longitude;
}
