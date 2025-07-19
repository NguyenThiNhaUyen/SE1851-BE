package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BloodComponentType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoveryRuleDTO {
    private Long recoveryRuleId;
    private BloodComponentType componentType;
    private int recoveryDays;
    private String note;
}
