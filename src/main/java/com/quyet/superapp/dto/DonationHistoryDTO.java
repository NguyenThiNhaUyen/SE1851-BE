package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationHistoryDTO {
    private LocalDate donationDate;
    private String location;
    private Integer volumeMl;
    private String bloodGroup;
    private String component;
    private String status;
}
