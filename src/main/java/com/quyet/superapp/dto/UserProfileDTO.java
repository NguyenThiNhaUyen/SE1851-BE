package com.quyet.superapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // ⚠️ Bỏ qua các field null trong JSON
public class UserProfileDTO {

    // 🧍 Thông tin cơ bản
    private Long userId;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    private String gender;
    private String occupation;

    @NotBlank(message = "CCCD không được để trống")
    @Pattern(regexp = "\\d{12}", message = "CCCD phải gồm đúng 12 chữ số")
    private String citizenId;

    // 📞 Thông tin liên hệ
    private String phone;
    private String landline;
    private String email;

    // 📍 Địa chỉ
    private Long addressId;              // Dùng để cập nhật
    private String addressFull;          // VD: "123 ABC, P.5, Q.10, TP.HCM"
    private AddressDTO address;          // Chi tiết nếu cần hiển thị
    private Double latitude;
    private Double longitude;

    // 🩸 Thông tin hiến máu & sức khỏe
    private Long bloodTypeId;            // ID nhóm máu
    private Double weight;
    private Double height;
    private String location;
    private LocalDateTime lastDonationDate;
    private Integer recoveryTime;        // Số ngày cần phục hồi
}
