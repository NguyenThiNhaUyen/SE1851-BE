package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Long userId;
    private String fullName;
    private String dob; // Chuỗi dd-MM-yyyy
    private String gender;
    private String bloodType;
    private String phone;
    private String addressFull; // VD: "12 Nguyễn Trãi, Phường 1, Quận 5, TP Hồ Chí Minh"
}
