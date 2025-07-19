package com.quyet.superapp.dto;

<<<<<<< HEAD
import lombok.Data;

@Data
public class AddressDTO {
    private String addressStreet; // Ví dụ: "12 Nguyễn Huệ"
    private Long wardId;          // chỉ dùng wardId ở phía gửi request
    private String ward;
    private String district;
=======
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AddressDTO {
    @NotBlank
    private String addressStreet;

    private Long wardId;
    @NotBlank
    private String ward;
    @NotBlank
    private String district;
    @NotBlank
>>>>>>> origin/main
    private String city;
}
