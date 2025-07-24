package com.quyet.superapp.dto;


import com.quyet.superapp.enums.BloodBagStatus;
import com.quyet.superapp.enums.RhType;
import com.quyet.superapp.enums.TestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BloodBagDTO {
    private Long bloodBagId;
    private String bagCode;

    private Long bloodTypeId;
    private String bloodTypeName;

    private RhType rh;
    private Integer volume;
    private Double hematocrit;
    private LocalDateTime collectedAt;

    private TestStatus testStatus;
    private BloodBagStatus status;

    private Long donorId;
    private String donorName;
    private String donorPhone;

    private String note;
}

