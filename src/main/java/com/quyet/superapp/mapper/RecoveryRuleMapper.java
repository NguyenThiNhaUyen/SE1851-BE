package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.RecoveryRuleDTO;
import com.quyet.superapp.entity.RecoveryRule;

public class RecoveryRuleMapper {

    public static RecoveryRuleDTO toDTO(RecoveryRule rule) {
        return RecoveryRuleDTO.builder()
                .recoveryRuleId(rule.getRecoveryRuleId())
                .componentType(rule.getComponentType())
                .recoveryDays(rule.getRecoveryDays())
                .note(rule.getNote())
                .build();
    }

    public static RecoveryRule toEntity(RecoveryRuleDTO dto) {
        return RecoveryRule.builder()
                .recoveryRuleId(dto.getRecoveryRuleId())
                .componentType(dto.getComponentType())
                .recoveryDays(dto.getRecoveryDays())
                .note(dto.getNote())
                .build();
    }
}
