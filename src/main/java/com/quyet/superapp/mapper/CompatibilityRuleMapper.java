package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.CompatibilityRuleDTO;
import com.quyet.superapp.entity.CompatibilityRule;

public class CompatibilityRuleMapper { //chưa sử dung
    public static CompatibilityRuleDTO toDTO(CompatibilityRule rule) {
        return new CompatibilityRuleDTO(
                rule.getCompatibilityRuleId(),
                rule.getDonorType().getBloodTypeId(),
                rule.getRecipientType().getBloodTypeId(),
                rule.getComponent().getBloodComponentId(),
                rule.getIsCompatible()
        );
    }
}