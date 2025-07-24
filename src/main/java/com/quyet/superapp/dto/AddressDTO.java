package com.quyet.superapp.dto;

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
    private String city;
}
