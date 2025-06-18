package com.quyet.superapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodComponentDTO {
    private Long bloodComponentId;
    private String name;

    //các feild mới
    private String code;
    private String storageTemp;
    private Integer storageDays;
    private String usage;
    private Boolean isApheresisCompatible;
}
