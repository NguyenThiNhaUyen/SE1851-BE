package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullDonationProcessRequest {
    private Long registrationId;
    private HealthCheckFormDTO healthCheckFormDTO;
    private PreDonationTestDTO preDonationTestDTO;
}
