package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullHealthCheckDTO {

    private HealthCheckFormDTO healthCheck;
    private PreDonationTestDTO preDonationTest;

}
