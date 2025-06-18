package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRequestDTO {
    private Long donationId;
    private Long userId;
    private Long registrationId;
    private Long bloodTypeId;
    private Long componentId;
    private LocalDate donationDate;
    private Integer volumeMl;
    private String location;
    private String notes;
    private Double weight;
}


